package kz.zhandos.machinelearning.chapter8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kz.zhandos.machinelearning.chapter3.Pair;

public class NumPredict {


  public double wineprice(Double rating, Double age) {
    double peak_age = rating - 50;
    // # Calculate price based on rating
    double price = rating / 2;
    if (age > peak_age) {
      // # Past its peak, goes bad in 5 years
      price = price * (5 - (age - peak_age));
    } else {
      // # Increases to 5x original value as it
      // # approaches its peak
      price = price * (5 * ((age + 1) / peak_age));
    }
    if (price < 0)
      price = 0;
    return price;
  }

  public double euclidean(List<?> v1, List<?> v2) {
    Double d = 0.0;
    for (int i = 0; i < v1.size(); i++) {
      d += Math.pow((((Number) v1.get(i)).doubleValue() - ((Number) v2.get(i)).doubleValue()), 2);
    }
    return Math.sqrt(d);
  }

  public Map<Integer, Double> getdistances(List<Map<String, List<Double>>> data,
      List<Double> vec1) {
    Map<Integer, Double> distancelist = new HashMap<Integer, Double>();
    for (int i = 0; i < data.size(); i++) {
      List<Double> vec2 = (List<Double>) data.get(i).get("input");
      distancelist.put(i, euclidean(vec1, vec2));
    }
    return distancelist;
  }


  public double knnestimate(List<Map<String, List<Double>>> data, List<Double> vec1, Integer k) {
    // # Get sorted distances
    k = k == null ? 3 : k;
    Map<Integer, Double> dlist = getdistances(data, vec1);
    double avg = 0.0;
    // # Take the average of the top k results
    for (Map.Entry<Integer, Double> e : dlist.entrySet()) {
      double idx = dlist.get(e.getKey());
      for (Double n : data.get((int) idx).get("result")) {
        avg += n;
      }
    }
    avg = avg / k;
    return avg;
  }


  public List<Map<String, List<Double>>> wineset1() {
    List<Map<String, List<Double>>> rows = new ArrayList<Map<String, List<Double>>>();
    Random random = new Random();
    for (int i = 0; i < 300; i++) {
      Map<String, List<Double>> row = new HashMap<String, List<Double>>();
      // # Create a random age and rating
      double rating = random.nextDouble() * 50 + 50;
      double age = random.nextDouble() * 50;
      // # Get reference price
      Double price = wineprice(rating, age);
      // # Add some noise
      price *= (random.nextDouble() * 0.4 + 0.8);
      // # Add to the dataset
      row.put("input", Arrays.asList(rating, age));
      row.put("result", Arrays.asList(price));
      rows.add(row);
    }
    return rows;
  }

  public double subtractweight(Double dist, Double cons) {
    cons = cons == null ? 1.0 : cons;
    if (dist > cons)
      return 0;
    else
      return cons - dist;
  }

  public double inverseweight(Double dist, Double num, Double cons) {
    num = num == null ? 1.0 : num;
    cons = cons == null ? 0.1 : cons;
    return num / (dist + cons);
  }


  public double gaussian(Double dist, Double sigma) {
    sigma = sigma == null ? 10.0 : sigma;
    // math.e**(-dist**2/(2*sigma**2))
    return Math.pow(Math.E, (-Math.pow(dist, 2) / (2 * Math.pow(sigma, 2))));
  }


  public double weightedknn(List<Map<String, List<Double>>> data, List<Double> vec1, Double k,
      WeightF weightF) {
    k = k == null ? 5 : k;
    int count = 0;
    // # Get distances
    Map<Integer, Double> dlist = getdistances(data, vec1);
    Double avg = 0.0;
    Double totalweight = 0.0;
    // # Get weighted average
    for (Map.Entry<Integer, Double> e : dlist.entrySet()) {
      Double dist = e.getValue();
      Integer idx = e.getKey();
      Double weight = 0.0;
      if (weightF == null)
        weight = gaussian(dist, null);
      for (Double w : data.get(idx).get("result")) {
        avg += weight * w;
      }
      totalweight += weight;
      if (count++ > k)
        break;
    }
    avg = avg / totalweight;
    return avg;
  }


  public Pair<List<Map<String, List<Double>>>, List<Map<String, List<Double>>>> dividedata(
      List<Map<String, List<Double>>> data, Double test) {
    test = test == null ? 0.05 : test;
    Random random = new Random();
    List<Map<String, List<Double>>> trainset = new ArrayList<Map<String, List<Double>>>();
    List<Map<String, List<Double>>> testset = new ArrayList<Map<String, List<Double>>>();
    for (Map<String, List<Double>> row : data)
      if (random.nextDouble() < test)
        testset.add(row);
      else
        trainset.add(row);
    return Pair.pair(trainset, testset);
  }


  public double testalgorithm(WeightF algf, List<Map<String, List<Double>>> trainset,
      List<Map<String, List<Double>>> testset) {
    double error = 0.0;
    for (Map<String, List<Double>> row : testset) {
      if (algf == WeightF.knnestimate) {
        double guess = knnestimate(trainset, row.get("input"), 3);
        error += Math.pow((row.get("result").get(0) - guess), 2);
      }
    }
    return error / testset.size();
  }


  public double crossvalidate(WeightF algf, List<Map<String, List<Double>>> data, Integer trials,
      Double test) {
    trials = trials == null ? 100 : trials;
    test = test == null ? 0.05 : test;
    double error = 0.0;
    for (int i = 0; i < trials; i++) {
      Pair<List<Map<String, List<Double>>>, List<Map<String, List<Double>>>> pair =
          dividedata(data, test);
      error += testalgorithm(algf, pair.first, pair.second);
    }
    return error / trials;
  }



  public static void main(String[] args) {
    NumPredict numpredict = new NumPredict();
    System.out.println(numpredict.wineprice(95.0, 3.0));
    System.out.println(numpredict.wineprice(95.0, 8.0));
    List<Map<String, List<Double>>> data = numpredict.wineset1();
    Map<String, List<Double>> data1 = new HashMap<String, List<Double>>();
    Map<String, List<Double>> data2 = new HashMap<String, List<Double>>();
    data1.put("input", Arrays.asList(82.720398223643514, 49.21295829683897));
    data2.put("input", Arrays.asList(98.942698715228076, 25.702723509372749));
    System.out.println(numpredict.euclidean(data1.get("input"), data2.get("input")));

    System.out.println(numpredict.knnestimate(data, Arrays.asList(95.0, 3.0), null));

    System.out.println(numpredict.knnestimate(data, Arrays.asList(95.0, 3.0), 1));


    System.out.println(numpredict.subtractweight(0.1, null));

    System.out.println(numpredict.inverseweight(0.1, null, null));

    System.out.println(numpredict.gaussian(3.0, null));

    System.out.println(numpredict.weightedknn(data, Arrays.asList(95.0, 5.0), null, null));

    System.out.println(numpredict.crossvalidate(WeightF.knnestimate, data, null, 1.0));

  }

}
