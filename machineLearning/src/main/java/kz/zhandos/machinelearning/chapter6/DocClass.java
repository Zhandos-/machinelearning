package kz.zhandos.machinelearning.chapter6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocClass {

  Map<String, Map<String, Integer>> fc;
  Map<String, Integer> cc;
  Map<String, Integer> thresholds;

  public DocClass() {
    fc = new HashMap<String, Map<String, Integer>>();
    cc = new HashMap<String, Integer>();
    thresholds = new HashMap<String, Integer>();
  }


  public List<String> getwords(String text) {
    List<String> ret = null;
    ret = text == null ? null
        : Arrays.asList(text.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+"));
    return ret;
  }

  // # Increase the count of a feature/category pair
  public void incf(String f, String cat) {
    if (fc.get(f) == null)
      fc.put(f, new HashMap<String, Integer>());
    if (!fc.get(f).containsKey(cat))
      fc.get(f).put(cat, 0);
    fc.get(f).put(cat, fc.get(f).get(cat) + 1);
  }

  // # Increase the count of a category
  public void incc(String cat) {
    if (!cc.containsKey(cat))
      cc.put(cat, 0);
    cc.put(cat, cc.get(cat) + 1);
  }


  // # The number of times a feature has appeared in a category
  public Integer fcount(String f, String cat) {
    if (fc.containsKey(f) && fc.get(f).containsKey(cat))
      return fc.get(f).get(cat);
    return 0;
  }


  // # The number of items in a category
  public int catcount(String cat) {
    if (cc.containsKey(cat))
      return cc.get(cat);
    return 0;
  }

  // # The total number of items
  public int totalcount() {
    return cc.size();
  }

  // # The list of all categories
  public List<String> categories() {
    return new ArrayList<String>(cc.keySet());
  }

  public void train(String item, String cat) {
    List<String> features = getwords(item);
    // # Increment the count for every feature with this category
    for (String f : features)
      incf(f, cat);
    // # Increment the count for this category
    incc(cat);
  }


  public int fprob(String f, String cat) {
    if (catcount(cat) == 0)
      return 0;
    return fcount(f, cat) / catcount(cat);

  }


  public double weightedprob(String f, String cat, Double weight, Double ap) {
    // # Calculate current probability
    weight = weight == null ? 1.0 : weight;
    ap = ap == null ? 0.5 : ap;

    int basicprob = fprob(f, cat);
    int totals = 0;
    for (String s : categories()) {
      totals += fcount(f, s);
    }
    double bp = (((weight * ap) + (totals * basicprob)) / (weight + totals));

    return bp;


  }

  public int docprob(String item, String cat) {
    List<String> features = getwords(item);
    // # Multiply the probabilities of all the features together
    int p = 1;
    for (String f : features)
      p *= weightedprob(f, cat, null, null);
    return p;
  }

  public double prob(String item, String cat) {
    int catprob = catcount(cat) / totalcount();
    int docprob = docprob(item, cat);
    return docprob * catprob;
  }

  public void setthreshold(String cat, Integer t) {
    thresholds.put(cat, t);
  }

  public int getthreshold(String cat) {
    if (!thresholds.containsKey(cat))
      return 1;
    return thresholds.get(cat);
  }


  public String classify(String item, String defaultt) {
    Map<String, Double> probs = new HashMap<String, Double>();
    // # Find the category with the highest probability
    double max = 0.0d;
    String best = null;
    for (String cat : categories()) {
      probs.put(cat, prob(item, cat));
      max = Math.max(probs.get(cat), max);
      best = cat;
    }

    // # Make sure the probability exceeds threshold*next best
    for (String cat : probs.keySet()) {
      if (cat == best)
        continue;
      if (probs.get(cat) * getthreshold(best) > probs.get(best))
        return defaultt;
    }
    return best;
  }


  public double fisherclassifier_cprob(String f, String cat) {
    // # The frequency of this feature in this category
    int clf = fprob(f, cat);
    if (clf == 0)
      return 0;
    // # The frequency of this feature in all the categories
    double freqsum = 0;
    for (String c : categories()) {
      freqsum += fprob(f, c);
    }
    // # The probability is the frequency in this category divided by
    // # the overall frequency
    double p = clf / (freqsum);
    return p;
  }


  public double fisherprob(String item, String cat) {
    // # Multiply all the probabilities together
    double p = 1;
    List<String> features = getwords(item);
    for (String f : features)
      p *= (weightedprob(f, cat, null, null));
    // # Take the natural log and multiply by -2
    double fscore = -2 * Math.log(p);
    // # Use the inverse chi2 function to get a probability
    return invchi2(fscore, features.size() * 2);
  }



  public double invchi2(double chi, int df) {
    double m = chi / 2.0;
    double term = Math.exp(-m);
    double sum = Math.exp(-m);
    for (int i = 0; i < df / 2; i++) {
      term *= m / i;
      sum += term;
    }
    return Math.min(sum, 1.0);
  }



  public static void main(String args[]) {
    DocClass cl = new DocClass();
    cl.train("the quick brown fox jumps over the lazy dog", "good");
    cl.train("make quick money in the online casino", "bad");
    cl.train("buy pharmaceuticals now", "bad");
    cl.train("make quick money at the online casino", "bad");
    cl.train("the quick brown fox jumps", "good");

    System.out.println(cl.fcount("quick", "good"));
    System.out.println(cl.fcount("quick", "bad"));


    System.out.println(cl.fprob("quick", "good"));

    System.out.println(cl.weightedprob("money", "good", null, null));

    System.out.println(cl.prob("quick rabbit", "good"));

    System.out.println(cl.prob("quick rabbit", "bad"));


    System.out.println(cl.classify("quick rabbit", "unknown"));

    System.out.println(cl.classify("quick money", "unknown"));


    System.out.println(cl.fisherclassifier_cprob("quick", "good"));


    System.out.println(cl.fisherclassifier_cprob("money", "bad"));

    System.out.println(cl.fisherprob("quick rabbit", "good"));

    System.out.println(cl.fisherprob("quick rabbit", "bad"));
  }


}
