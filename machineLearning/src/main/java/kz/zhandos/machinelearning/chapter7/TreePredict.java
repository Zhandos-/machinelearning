package kz.zhandos.machinelearning.chapter7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import kz.zhandos.machinelearning.chapter3.Pair;

public class TreePredict {

  List<Object[]> my_data = new ArrayList<Object[]>();

  public TreePredict() throws FileNotFoundException {
    File input = new File(TreePredict.class.getResource("decision_tree_example.txt").getPath());
    Scanner s = new Scanner(input);
    Scanner line = null;
    while (s.hasNextLine()) {
      String r = s.nextLine();
      Object[] row = new Object[5];
      line = new Scanner(r);
      line.useDelimiter("\\t");
      int i = 0;
      while (line.hasNext()) {
        if (line.hasNextBoolean()) {
          row[i++] = line.nextBoolean();
        } else if (line.hasNextInt()) {
          row[i++] = line.nextInt();
        } else {
          row[i++] = line.next();
        }
      }
      line.close();
      my_data.add(row);
    }
    s.close();
  }

  // # Divides a set on a specific column. Can handle numeric
  // # or nominal values
  public Pair<List<Object[]>, List<Object[]>> divideset(List<Object[]> rows, int column,
      Object value) {
    // # Make a function that tells us if a row is in
    // # the first group (true) or the second group (false)
    List<Object[]> split_function = new ArrayList<Object[]>();
    List<Object[]> set1 = new ArrayList<Object[]>();
    List<Object[]> set2 = new ArrayList<Object[]>();
    if (value instanceof Number) {
      for (Object[] m : rows) {
        if (((Number) m[column]).doubleValue() > ((Number) value).doubleValue())
          split_function.add(m);
      }
    } else {
      for (Object[] m : rows) {
        if (m[column].equals(value))
          split_function.add(m);
      }
    }

    // # Divide the rows into two sets and return them
    for (Object[] m : rows) {
      if (split_function.contains(m)) {
        set1.add(m);
      } else {
        set2.add(m);
      }
    }
    return Pair.pair(set1, set2);
  }

  public static void print(List<Object[]> my_data) {
    for (Object[] line : my_data) {
      for (Object word : line) {
        System.out.print(word + "\t");
      }
      System.out.println();
    }
  }


  // # Create counts of possible results (the last column of
  // # each row is the result)
  public Map<Object, Integer> uniquecounts(List<Object[]> rows) {
    Map<Object, Integer> results = new HashMap<Object, Integer>();
    for (Object[] row : rows) {
      // # The result is the last column
      String r = (String) row[row.length - 1];
      if (!results.containsKey(r))
        results.put(r, 0);
      results.put(r, results.get(r) + 1);
    }
    return results;
  }

  // # Probability that a randomly placed item will
  // # be in the wrong category
  public double giniimpurity(List<Object[]> rows) {
    double total = rows.size();
    Map<Object, Integer> counts = uniquecounts(rows);
    Double imp = 0d;
    for (Map.Entry<Object, Integer> k1 : counts.entrySet()) {
      Double p1 = ((Number) (k1.getValue() / total)).doubleValue();
      for (Map.Entry<Object, Integer> k2 : counts.entrySet()) {
        if (k1.equals(k2))
          continue;
        Double p2 = ((Number) (k2.getValue() / total)).doubleValue();
        imp += p1 * p2;
      }
    }
    return imp;
  }

  // # Entropy is the sum of p(x)log(p(x)) across all
  // # the different possible results
  public double entropy(List<Object[]> rows) {
    Map<Object, Integer> results = uniquecounts(rows);
    // # Now calculate the entropy
    double ent = 0.0;
    double size = rows.size();
    for (Object r : results.keySet()) {
      double p = results.get(r) / size;
      ent = ent - p * Math.log(p) / Math.log(2);
    }
    return ent;
  }


  public DecisionNode buildtree(List<Object[]> rows, ScoreF scoref) {
    if (rows.size() == 0)
      return new DecisionNode(null, null, null, null, null);
    if (scoref == null)
      scoref = ScoreF.Entropy;

    // # Set up some variables to track the best criteria
    double current_score = 0.0;
    double best_gain = 0.0;
    Pair<Integer, Object> best_criteria = null;
    Pair<List<Object[]>, List<Object[]>> best_sets = null;
    if (scoref == ScoreF.Entropy) {
      current_score = entropy(rows);
    }


    for (int col = 0; col < rows.get(0).length; col++) {
      // # Generate the list of different values in
      // # this column
      Map<Object, Double> column_values = new HashMap<Object, Double>();
      for (Object[] row : rows) {
        column_values.put(row[col], 1d);
      }
      // # Now try dividing the rows up for each value
      // # in this column
      for (Object value : column_values.keySet()) {
        Pair<List<Object[]>, List<Object[]>> pair = divideset(rows, col, value);
        List<Object[]> set1 = pair.first;
        List<Object[]> set2 = pair.second;
        // # Information gain
        double set1Scoref = 0;
        double set2Scoref = 0;
        double p = set1.size() / (double) rows.size();
        if (scoref == ScoreF.Entropy) {
          set1Scoref = entropy(set1);
          set2Scoref = entropy(set2);
        }
        double gain = current_score - p * set1Scoref - (1 - p) * set2Scoref;
        if (gain > best_gain && set1.size() > 0 && set2.size() > 0) {
          best_gain = gain;
          best_criteria = Pair.pair(col, value);
          best_sets = Pair.pair(set1, set2);
        }
      }
    }
    // # Create the subbranches
    if (best_gain > 0) {
      DecisionNode trueBranch = buildtree(best_sets.first, scoref);
      DecisionNode falseBranch = buildtree(best_sets.second, scoref);
      return new DecisionNode(best_criteria.first, best_criteria.second, null, trueBranch,
          falseBranch);
    } else {
      return new DecisionNode(null, null, uniquecounts(rows), null, null);
    }

  }


  public void printtree(DecisionNode tree, String indent) {
    indent = indent == null ? " " : indent;
    // # Is this a leaf node?

    if (tree.results != null)
      System.out.print(tree.results + "\n");
    else {
      // #Print the criteria
      System.out.print(tree.col + ":" + tree.value + "? " + "\n");
      // # Print the branches
      System.out.print(indent + "T->");
      printtree(tree.tb, indent + indent);
      System.out.print(indent + "F->");
      printtree(tree.fb, indent + indent);
    }

  }

  public Map<Object, Integer> classify(Object[] observation, DecisionNode tree) {
    if (tree.results != null)
      return tree.results;
    else {
      Object v = observation[tree.col];
      DecisionNode branch = null;
      if (v instanceof Number)
        if (((Number) v).doubleValue() >= ((Number) tree.value).doubleValue())
          branch = tree.tb;
        else
          branch = tree.fb;
      else if (v.equals(tree.value))
        branch = tree.tb;
      else
        branch = tree.fb;
      return classify(observation, branch);
    }

  }

  public void prune(DecisionNode tree, Double mingain) {
    // # If the branches aren't leaves, then prune them
    if (tree.tb.results == null)
      prune(tree.tb, mingain);
    if (tree.fb.results == null)
      prune(tree.fb, mingain);

    // # If both the subbranches are now leaves, see if they
    // # should merged
    if (tree.tb.results != null && tree.fb.results != null) {
      // # Build a combined dataset
      List<Object[]> tb = new ArrayList<Object[]>();
      List<Object[]> fb = new ArrayList<Object[]>();

      for (Map.Entry<Object, Integer> e : tree.tb.results.entrySet()) {
        // tb+=[[v]]*c
        Object[] tmp = new Object[e.getValue()];
        for (int i = 0; i < e.getValue(); i++)
          tmp[i] = e.getKey();
        tb.add(tmp);
      }

      for (Map.Entry<Object, Integer> e : tree.fb.results.entrySet()) {
        // fb+=[[v]]*c
        Object[] tmp = new Object[e.getValue()];
        for (int i = 0; i < e.getValue(); i++)
          tmp[i] = e.getKey();
        fb.add(tmp);
      }

      // # Test the reduction in entropy
      List<Object[]> newList = new ArrayList<Object[]>();
      newList.addAll(fb);
      newList.addAll(tb);
      double delta = entropy(newList) - (entropy(tb) + entropy(fb) / 2);
      if (delta < mingain) {
        // # Merge the branches
        tree.tb = null;
        tree.fb = null;
        tree.results = uniquecounts(newList);
      }
    }
  }


  public Map<Object, Integer> mdclassify(Object[] observation, DecisionNode tree) {
    DecisionNode branch = null;
    if (tree.results != null)
      return tree.results;
    else {
      Object v = observation[tree.col];
      if (v == null) {
        Map<Object, Integer> tr = mdclassify(observation, tree.tb);
        Map<Object, Integer> fr = mdclassify(observation, tree.fb);
        double tcount = tr.size();
        double fcount = fr.size();
        double tw = tcount / (tcount + fcount);
        double fw = fcount / (tcount + fcount);
        Map<Object, Integer> result = new HashMap<Object, Integer>();
        for (Map.Entry<Object, Integer> e : tr.entrySet()) {
          result.put(e.getKey(), (int) (e.getValue() * tw));
        }
        for (Map.Entry<Object, Integer> e : fr.entrySet()) {
          result.put(e.getKey(), (int) (e.getValue() * fw));
        }
        return result;
      } else {
        if (v instanceof Number) {
          if (((Number) v).doubleValue() >= ((Number) tree.value).doubleValue())
            branch = tree.tb;
          else
            branch = tree.fb;
        } else {
          if (v.equals(tree.value))
            branch = tree.tb;
          else
            branch = tree.fb;
        }
      }
      return mdclassify(observation, branch);
    }
  }



  public static void main(String args[]) throws FileNotFoundException {
    TreePredict predict = new TreePredict();

    // print();


    System.out.println(predict.giniimpurity(predict.my_data));
    System.out.println(predict.entropy(predict.my_data));
    List<Object[]> set1 = predict.divideset(predict.my_data, 2, "yes").first;
    System.out.println(predict.entropy(set1));
    // predict.print(set1);
    System.out.println(predict.giniimpurity(set1));
    System.out.println(predict.buildtree(predict.my_data, ScoreF.Entropy));
    predict.printtree(predict.buildtree(predict.my_data, ScoreF.Entropy), " ");

    System.out.println(predict.classify(new Object[] {"(direct)", "USA", "yes", 5, "None"},
        predict.buildtree(predict.my_data, ScoreF.Entropy)));


    DecisionNode node = predict.buildtree(predict.my_data, ScoreF.Entropy);
    predict.prune(node, 2.0);

    predict.printtree(node, "-");


    System.out.println(predict.mdclassify(new Object[] {"google", "France", "yes", 5, "None"},
        predict.buildtree(predict.my_data, ScoreF.Entropy)));

  }


}
