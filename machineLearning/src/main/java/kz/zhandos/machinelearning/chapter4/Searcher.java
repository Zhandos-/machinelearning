package kz.zhandos.machinelearning.chapter4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import kz.zhandos.machinelearning.chapter3.Pair;
import kz.zhandos.machinelearning.mybatis.SearchDAO;
import kz.zhandos.machinelearning.mybatis.SearchMapper;

public class Searcher {

  private final static Logger log = Logger.getLogger(Searcher.class);

  private final static SearchDAO DAO = new SearchDAO();
  private final static SearchMapper MAPPER = DAO.getMapper();


  public static Pair<List<Long>, List<HashMap<?, ?>>> getmatchrows(String query) {
    // # Strings to build the query
    // fieldlist='w0.urlid'
    // tablelist=''
    // clauselist=''
    // wordids=[]

    Pair<List<Long>, List<HashMap<?, ?>>> ret = null;

    String fieldlist = "w0.urlid";
    String tablelist = "";
    String clauselist = "";
    List<Long> wordids = new ArrayList<Long>();


    // # Split the words by spaces
    // words=q.split(' ')
    // tablenumber=0
    String words[] = query.split(" ");
    int tablenumber = 0;


    // for word in words:
    // # Get the word ID
    // wordrow=self.con.execute(
    // "select rowid from wordlist where word='%s'" % word).fetchone( )
    // if wordrow!=None:
    // wordid=wordrow[0]
    // wordids.append(wordid)
    // if tablenumber>0:
    // tablelist+=','
    // clauselist+=' and '
    // clauselist+='w%d.urlid=w%d.urlid and ' % (tablenumber-1,tablenumber)
    // fieldlist+=',w%d.location' % tablenumber
    // tablelist+='wordlocation w%d' % tablenumber
    // clauselist+='w%d.wordid=%d' % (tablenumber,wordid)
    // tablenumber+=1

    for (String word : words) {
      Long wordid = MAPPER.getEntryId("wordlist", "word", word);
      if (wordid != null) {
        wordids.add(wordid);
        if (tablenumber > 0) {
          tablelist += ",";
          clauselist += " and ";
          clauselist += String.format("w%d.urlid=w%d.urlid and ", tablenumber - 1, tablenumber);
        }
        fieldlist += String.format(",w%d.location", tablenumber);
        tablelist += String.format("wordlocation w%d", tablenumber);
        clauselist += String.format("w%d.wordid=%d", tablenumber, wordid);
        tablenumber += 1;
      }
    }
    String fullquery =
        String.format("select %s from %s where %s", fieldlist, tablelist, clauselist);

    List<HashMap<?, ?>> rows = MAPPER.runSQL(fullquery);
    ret = Pair.pair(wordids, rows);
    return ret;
  }



  public static void main(String args[]) {
    Searcher searcher = new Searcher();
    System.out.println(searcher.getmatchrows("Programming language"));
  }



}
