package kz.zhandos.machinelearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PearsonCorrelation {

	public static double distance(Map<String, Map<String, Double>> critics, String critic1, String critic2) {
		double ret = 0;
		List<String> commonFilms = new ArrayList<String>();

		Map<String, Double> films2 = critics.get(critic2);
		Map<String, Double> films1 = critics.get(critic1);
		double sum1 = 0d;
		double sum2 = 0d;
		double sumSqr1 = 0d;
		double sumSqr2 = 0d;
		double pSum = 0d;
		double num = 0d;
		double den = 0d;

		for (String film : critics.get(critic1).keySet()) {
			if (films2.containsKey(film))
				commonFilms.add(film);
		}

		int n = commonFilms.size();

		if (n > 0) {
			for (String film : commonFilms) {
				sum1 += films1.get(film);
				sum2 += films2.get(film);
				sumSqr1 += Math.pow(films1.get(film), 2);
				sumSqr2 += Math.pow(films2.get(film), 2);
				pSum += films1.get(film) * films2.get(film);
			}
			num = pSum - ((sum1 * sum2) / n);
			den = Math.sqrt((sumSqr1 - Math.pow(sum1, 2) / n) * (sumSqr2 - Math.pow(sum2, 2) / n));
			if (den == 0)
				ret = 0;
			else
				ret = num / den;
		}

		return ret;
	}

	public static Map<String, Double> topMathes(Map<String, Map<String, Double>> critics, String critic, int number) {
		Map<String, Double> ret = new HashMap<String, Double>();
		for (String person : critics.keySet()) {
			if (!person.equalsIgnoreCase(critic))
				ret.put(person, distance(critics, critic, person));
		}

		return ret;
	}

	public static Map<String, Double> recomendations(Map<String, Map<String, Double>> critics, String critic) {
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

}
