package kz.zhandos.machinelearning.chapter2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EuclideanDistance {

  public static double distance(Map<String, Map<String, Double>> critics, String critic1,
      String critic2) {
    double ret = 0;
    List<String> commonFilms = new ArrayList<String>();
    Map<String, Double> films2 = critics.get(critic2);
    Map<String, Double> films1 = critics.get(critic1);
    for (String film : critics.get(critic1).keySet()) {
      if (films2.containsKey(film))
        commonFilms.add(film);
    }
    if (commonFilms.size() > 0) {
      for (String film : commonFilms) {
        ret += Math.pow(films1.get(film) - films2.get(film), 2);
      }
      ret = 1 / (1 + ret);
    }
    return ret;
  }


  public static Map<String, Double> topMathes(Map<String, Map<String, Double>> critics,
      String critic, int number) {
    Map<String, Double> ret = new HashMap<String, Double>();
    for (String person : critics.keySet()) {
      if (!person.equalsIgnoreCase(critic))
        ret.put(person, distance(critics, critic, person));
    }

    return ret;
  }

  public static Map<String, Double> recomendations(Map<String, Map<String, Double>> critics,
      String critic) {
    Map<String, Double> ret = new HashMap<String, Double>();
    for (String person : critics.keySet()) {
      if (!person.equalsIgnoreCase(critic)) {
        double sim = distance(critics, critic, person);
        if (sim < 0)
          continue;

      }
    }
    return ret;
  }


  public static Map<String, Map<String, Double>> calculateSimilarItems(
      Map<String, Map<String, Double>> movies) {
    Map<String, Map<String, Double>> ret = new HashMap<String, Map<String, Double>>();
    // c=0
    // for item in itemPrefs:
    // # Status updates for large datasets
    // c+=1
    // if c%100==0: print "%d / %d" % (c,len(itemPrefs))
    // # Find the most similar items to this one
    // scores=topMatches(itemPrefs,item,n=n,similarity=sim_distance)
    // result[item]=scores
    int c = 0;
    for (Map.Entry<String, Map<String, Double>> e : movies.entrySet()) {
      c++;
      if (c % 100 == 0)
        System.out.println(c + " / " + e.getValue().size());
      ret.put(e.getKey(), topMathes(movies, e.getKey(), 5));
    }

    return ret;
  }
}
