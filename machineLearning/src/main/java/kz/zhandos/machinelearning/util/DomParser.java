package kz.zhandos.machinelearning.util;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DomParser {

  public static Document parceUrl(String url) throws Exception {
    Document doc = null;
    try {
      doc = Jsoup.connect("https://en.wikipedia.org/wiki/Programming_language").get();
    } catch (Exception e) {
      throw e;
    }
    return doc;
  }

  private static String trim(String s, int width) {
    if (s.length() > width)
      return s.substring(0, width - 1) + ".";
    else
      return s;
  }



  public static void main(String[] args) throws Exception {

    try {
      File input = new File(DomParser.class.getResource("1.html").getPath());
      Document doc;
      // doc = Jsoup.parse(input, "UTF-8");
      doc = Jsoup.connect("https://en.wikipedia.org/wiki/Programming_language").get();
      // System.out.println(doc);
      Elements content = doc.select("a[href]");
      for (Element element : content) {
        String linkHref = element.attr("href");
        String linkText = element.text();
        System.out.println(linkText);
        System.out.println(
            String.format(" * a: <%s>  (%s)", element.attr("abs:href"), trim(element.text(), 35)));
      }
    } catch (Exception e) {
      throw e;
    }
  }
}
