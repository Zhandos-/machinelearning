package kz.zhandos.machinelearning;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EuclideanDistance {

	public static double distance(Map<String, Map<String, Double>> critics, String critic1, String critic2) {
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
}
