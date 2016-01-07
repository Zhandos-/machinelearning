package kz.zhandos.machinelearning.chapter9;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class MatchMaker {


  public List<String[]> readCSV(String file) throws IOException {
    List<String[]> ret = new ArrayList<String[]>();
    CSVReader reader = null;
    try {
      reader = new CSVReader(new FileReader(MatchMaker.class.getResource(file).getFile()));
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
        ret.add(nextLine);
      }
    } catch (Exception e) {
    } finally {
      if (reader != null)
        reader.close();
    }
    return ret;
  }


  public Map<Integer, Double[]> lineartrain(List<String[]> rows) {
    Map<Integer, Double[]> averages = new HashMap<Integer, Double[]>();
    Map<Integer, Double> counts = new HashMap<Integer, Double>();
    int k = 0;
    for (String[] row : rows) {
      // # Get the class of this point
      int cl = k++;
      if (!averages.containsKey(cl)) {
        Double d[] = new Double[rows.size()];
        Arrays.fill(d, 0d);
        averages.put(cl, d);
      }
      if (!counts.containsKey(cl))
        counts.put(cl, 0d);
      // # Add this point to the averages
      for (int i = 0; i < row.length; i++) {
        averages.get(cl)[i] += Double.parseDouble(row[i]);
      }
      counts.put(cl, counts.get(cl) == null ? 0d : counts.get(cl) + 1);
    }


    // # Divide sums by counts to get the averages
    for (Map.Entry<Integer, Double[]> e : averages.entrySet())
      for (int i = 0; i < e.getValue().length; i++) {
        e.getValue()[i] /= counts.get(e.getKey());
      }
    return averages;
  }



  public static void main(String args[]) throws IOException {
    MatchMaker maker = new MatchMaker();
    // for (String[] string : maker.readCSV("agesonly.csv")) {
    // for (String string2 : string) {
    // System.out.print(string2 + " ");
    // }
    // System.out.println();
    // }
    System.out.println(maker.lineartrain(maker.readCSV("agesonly.csv")));
  }

}
