package kz.zhandos.machinelearning.util;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DomParser {

  public static void main(String[] args) throws Exception {

    try {
      File input = new File(DomParser.class.getResource("1.html").getPath());
      Document doc;
      doc = Jsoup.parse(input, "UTF-8");
      Elements content = doc.getElementsByClass("links");
      for (Element element : content) {
        for (Element link : element.getElementsByClass("title")) {
          String linkHref = link.attr("href");
          String linkText = link.text();
          System.out.println(linkText);
        }
      }
    } catch (Exception e) {
      throw e;
    }
  }
}
