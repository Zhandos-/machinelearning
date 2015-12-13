package kz.zhandos.machinelearning.chapter4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kz.zhandos.machinelearning.chapter3.Clusters;
import kz.zhandos.machinelearning.mybatis.SearchDAO;
import kz.zhandos.machinelearning.mybatis.SearchMapper;
import kz.zhandos.machinelearning.util.DomParser;

public class Crawler {
  private final static Logger log = Logger.getLogger(Clusters.class);

  private final static SearchDAO DAO = new SearchDAO();
  private final static SearchMapper MAPPER = DAO.getMapper();
  private static final List<String> IGNORE_WORDS = new ArrayList<String>();

  static {
    IGNORE_WORDS.addAll(Arrays.asList("the", "of", "to", "and", "a", "in", "is", "it"));
  }

  public void init(String DbName) {

  }

  public void delete() {

  }


  public Long getentryid(String table, String field, String value) {
    log.info(String.format(
        "Вызво метода getentryid(String table, String field, String value) table=%s field=%s value=%s",
        table, field, value));
    // cur=self.con.execute(
    // "select rowid from %s where %s='%s'" % (table,field,value))
    // res=cur.fetchone()
    // if res==None:
    // cur=self.con.execute(
    // "insert into %s (%s) values ('%s')" % (table,field,value))
    // return cur.lastrowid
    // else:
    // return res[0]
    Long ret = MAPPER.getEntryId(table, field, value);
    if (ret == null) {
      MAPPER.insTableValue(table, field, value);
      DAO.commitChanges();
    }
    ret = MAPPER.getEntryId(table, field, value);
    return ret;
  }


  // # Index an individual page
  // def addtoindex(self,url,soup):
  // print 'Indexing %s' % url
  //

  public void addtoindex(String url, Document soup) {
    if (isIndexed(url))
      return;
    log.info(String.format("Indexing %s", url));
    // if self.isindexed(url): return
    // print 'Indexing '+url
    // # Get the individual words
    // text=self.gettextonly(soup)
    String text = gettextonly(soup);
    List<String> words = separatewords(text);

    Long urlId = getentryid("urllist", "url", url);
    System.out.println("words size=" + words.size());
    for (int i = 0; i < words.size(); i++) {
      String word = words.get(i);
      if (!IGNORE_WORDS.contains(word)) {
        Long wordId = getentryid("wordlist", "word", word);
        MAPPER.insWordLocation(urlId, wordId, i);
      }
    }
    DAO.commitChanges();
    // words=self.separatewords(text)
    // # Get the URL id
    // urlid=self.getentryid('urllist','url',url)
    // # Link each word to this url
    // for i in range(len(words)):
    // word=words[i]
    // if word in ignorewords: continue
    // wordid=self.getentryid('wordlist','word',word)
    // self.con.execute("insert into wordlocation(urlid,wordid,location) \
    // values (%d,%d,%d)" % (urlid,wordid,i))



  }



  // # Extract the text from an HTML page (no tags)
  // def gettextonly(self,soup):
  // return None

  public String gettextonly(Element soup) {
    String ret = soup == null ? null : soup.text();
    return ret;
  }

  // # Separate the words by any non-whitespace character
  // def separatewords(self,text):
  // return None
  public List<String> separatewords(String text) {
    List<String> ret = null;
    ret = text == null ? null
        : Arrays.asList(text.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+"));
    return ret;
  }


  // #Return true if this
  // url is
  // already indexed

  // def isindexed(self,url):
  // return False
  // # Add a link between two pages

  public boolean isIndexed(String url) {
    if (MAPPER.isIndexedUrl(url)) {
      return MAPPER.isCrawled(url);
    }
    return false;
  }


  // def addlinkref(self,urlFrom,urlTo,linkText):
  // pass
  public void addlinkref(String urlFrom, String urlTo, String linkText) {

  }

  // # Starting with a list of pages, do a breadth
  // # first search to the given depth, indexing pages
  // # as we go
  // def crawl(self,pages,depth=2):
  // pass
  public List<String> crawl(List<String> pages, Integer depth) {
    List<String> ret = new ArrayList<String>();
    depth = depth == null ? 2 : depth;
    // for i in range(depth):
    // newpages=set( )
    // for page in pages:
    // try:
    // c=urllib2.urlopen(page)
    // except:
    // print "Could not open %s" % page
    // continue
    // soup=BeautifulSoup(c.read( ))
    // self.addtoindex(page,soup)
    // links=soup('a')
    // for link in links:
    // if ('href' in dict(link.attrs)):
    // url=urljoin(page,link['href'])
    // if url.find("'")!=-1: continue
    // url=url.split('#')[0] # remove location portion
    // if url[0:4]=='http' and not self.isindexed(url):
    // newpages.add(url)
    // linkText=self.gettextonly(link)
    // self.addlinkref(page,url,linkText)
    // self.dbcommit( )
    // pages=newpages
    for (int i = 0; i < pages.size(); i++) {
      for (String page : pages) {
        try {
          Document soup = DomParser.parceUrl(page);
          addtoindex(page, soup);
          Elements links = soup.select("a[href]");
          for (Element link : links) {
            String url = link.attr("abs:href");
            if (!isIndexed(url))
              ret.add(url);
            String linkText = gettextonly(link);
            addlinkref(page, url, linkText);
          }
        } catch (Exception e) {
          log.error(String.format("Could not open %s", page), e);
          continue;
        }
      }
    }
    return ret;
  }



  // # Create the database tables
  // def createindextables(self):
  // pass
  public void createindextables() {

  }



  public static void main(String args[]) {
    Crawler crawler = new Crawler();
    System.out
        .println(crawler.crawl(Arrays.asList("https://en.wikipedia.org/wiki/Programming_language",
            "https://en.wikipedia.org/wiki/Kazakhstan",
            "https://en.wikipedia.org/wiki/Java_(programming_language)"), null));
  }

}
