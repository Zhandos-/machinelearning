package kz.zhandos.machinelearning.chapter4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kz.zhandos.machinelearning.chapter3.Clusters;
import kz.zhandos.machinelearning.util.DomParser;

public class Crawler {
  private final static Logger log = Logger.getLogger(Clusters.class);

  public void init(String DbName) {

  }

  public void delete() {

  }


  public void getentryid(String table, String field, String value) {
    // return null;
  }


  // # Index an individual page
  // def addtoindex(self,url,soup):
  // print 'Indexing %s' % url
  //

  public void addtoindex(String url, Document soup) {
    log.info(String.format("Indexing %s", url));
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
}
