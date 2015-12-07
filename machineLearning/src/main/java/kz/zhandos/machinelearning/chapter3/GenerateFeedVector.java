package kz.zhandos.machinelearning.chapter3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class GenerateFeedVector {
  private final static Logger log = Logger.getLogger(GenerateFeedVector.class);
  Pattern p = Pattern.compile("<[^>]+>");

  private static List<String> getWords(String html) {
    log.info("Вызов List<String> getWords(String html)");
    String str = html.replaceAll("<[^>]+>", "");
    str = str.toLowerCase();
    log.info("конец вызова List<String> getWords(String html)");
    return Arrays.asList(str.split("[^A-Z^a-z]+"));
  }

  private static Map<String, Map<String, Integer>> getWordCounts(String url) throws Exception {
    // # Returns title and dictionary of word counts for an RSS feed
    // def getwordcounts(url):
    // # Parse the feed
    // d=feedparser.parse(url)
    // wc={}
    // # Loop over all the entries
    // for e in d.entries:
    // if 'summary' in e: summary=e.summary
    // else: summary=e.description
    // # Extract a list of words
    // words=getwords(e.title+' '+summary)
    // for word in words:
    // wc.setdefault(word,0)
    // wc[word]+=1
    // return d.feed.title,wc
    Map<String, Map<String, Integer>> ret = new HashMap<String, Map<String, Integer>>();
    try {
      List<Pair<String, String>> blogTitleWords = parseFeed(url);
      for (Pair<String, String> pair : blogTitleWords) {
        List<String> words = getWords(pair.first + " " + pair.second);
        Map<String, Integer> wc = new HashMap<String, Integer>();
        for (String word : words) {
          if (!wc.containsKey(word))
            wc.put(word, 0);
          else
            wc.put(word, wc.get(word) + 1);
        }
        ret.put(pair.first, wc);
      }
    } catch (Exception e) {
      log.error(e);
    }
    return ret;
  }


  public static List<Pair<String, String>> parseFeed(String urlPath) throws Exception {
    List<Pair<String, String>> blogTitleDescription = new ArrayList<Pair<String, String>>();
    URL url = null;
    HttpURLConnection httpcon = null;
    try {
      // Reading the feed
      url = new URL(urlPath);
      httpcon = (HttpURLConnection) url.openConnection();
      SyndFeedInput input = new SyndFeedInput();
      SyndFeed feed = input.build(new XmlReader(httpcon));
      List entries = feed.getEntries();
      Iterator itEntries = entries.iterator();
      while (itEntries.hasNext()) {
        SyndEntry entry = (SyndEntry) itEntries.next();
        blogTitleDescription.add(Pair.pair(entry.getTitle(), entry.getDescription().getValue()));
      }
    } catch (Exception e) {
      log.error(e);
      throw e;
    } finally {
      if (httpcon != null)
        httpcon.disconnect();
    }
    return blogTitleDescription;
  }


  public static List<String> readFile(String filePath) throws FileNotFoundException {
    List<String> ret = new ArrayList<String>();
    File input = new File(GenerateFeedVector.class.getResource(filePath).getPath());
    Scanner s = new Scanner(input);
    while (s.hasNextLine()) {
      ret.add(s.nextLine());
    }
    s.close();
    return ret;
  }



  public static void countWordInBlog(String toRead, String toWrite) throws Exception {
    // apcount={}
    // wordcounts={}
    // for feedurl in file('feedlist.txt'):
    // title,wc=getwordcounts(feedurl)
    // wordcounts[title]=wc
    // for word,count in wc.items( ):
    // apcount.setdefault(word,0)
    // if count>1:
    // apcount[word]+=1
    // wordlist=[]
    // for w,bc in apcount.items( ):
    // frac=float(bc)/len(feedlist)
    // if frac>0.1 and frac<0.5: wordlist.append(w)
    // out=file('blogdata.txt','w')
    // out.write('Blog')
    // for word in wordlist: out.write('\t%s' % word)
    // out.write('\n')
    // for blog,wc in wordcounts.items( ):
    // out.write(blog)
    // for word in wordlist:
    // if word in wc: out.write('\t%d' % wc[word])
    // else: out.write('\t0')
    // out.write('\n')
    PrintWriter out = null;
    try {
      List<String> blogLinks = readFile(toRead);

      Map<String, Integer> apcount = new HashMap<String, Integer>();
      Map<String, Map<String, Integer>> wordcounts = new HashMap<String, Map<String, Integer>>();
      List<String> wordList = new ArrayList<String>();


      for (String url : blogLinks) {
        Map<String, Map<String, Integer>> blogWordCounts = getWordCounts(url);
        wordcounts.putAll(blogWordCounts);
        for (Map.Entry<String, Map<String, Integer>> e : blogWordCounts.entrySet()) {
          for (Map.Entry<String, Integer> e1 : e.getValue().entrySet()) {
            if (!apcount.containsKey(e1.getKey())) {
              apcount.put(e1.getKey(), 0);
            }
            if (e1.getValue() > 1)
              apcount.put(e1.getKey(), e1.getValue() + 1);
          }
        }
      }

      int feedlistCount = blogLinks.size();
      for (Map.Entry<String, Integer> e : apcount.entrySet()) {
        double frac = e.getValue() / feedlistCount;
        // if (frac > 0.1 && frac < 0.5)
        wordList.add(e.getKey());
      }

      File file = new File(toWrite);

      out = new PrintWriter(new BufferedWriter(
          new FileWriter(GenerateFeedVector.class.getResource(toWrite).getPath(), true)));
      out.write("Blog");
      // for word in wordlist: out.write('\t%s' % word)
      // out.write('\n')
      // for blog,wc in wordcounts.items( ):
      // out.write(blog)
      // for word in wordlist:
      // if word in wc: out.write('\t%d' % wc[word])
      // else: out.write('\t0')
      // out.write('\n')


      for (String word : wordList) {
        out.write(String.format("\t%s", word));
      }
      out.write("\n");

      for (Map.Entry<String, Map<String, Integer>> e : wordcounts.entrySet()) {
        out.write(e.getKey());
        for (String word : wordList) {
          if (e.getValue().containsKey(word))
            out.write(String.format("\t%d", e.getValue().get(word)));
          else
            out.write("\t0");
        }
        out.write("\n");
      }

    } catch (Exception e) {
      log.error(e);
    } finally {
      if (out != null)
        out.close();
    }

  }

  public static void main(String args[]) throws Exception {
    countWordInBlog("feedlistLight.txt", "blogdataLight.txt");
  }

}
