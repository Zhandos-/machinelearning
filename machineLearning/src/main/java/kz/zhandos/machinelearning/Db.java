package kz.zhandos.machinelearning;

import java.util.HashMap;
import java.util.Map;

public class Db {
	public static final Map<String, Map<String, Double>> CRITICS = new HashMap<String, Map<String, Double>>();

	static {

		Map<String, Double> lisa = new HashMap<String, Double>();
		{
			// 'Lisa Rose': {'Lady in the Water': 2.5, 'Snakes on a Plane':
			// 3.5,
			// 'Just My Luck': 3.0, 'Superman Returns': 3.5, 'You, Me and
			// Dupree':
			// 2.5,
			// 'The Night Listener': 3.0},
			lisa.put("Lady in the Water", 2.5);
			lisa.put("Snakes on a Plane", 3.5);
			lisa.put("Just My Luck", 3.0);
			lisa.put("Superman Returns", 3.5);
			lisa.put("You, Me and Dupree", 2.5);
			lisa.put("The Night Listener", 3.0);
		}
		CRITICS.put("Lisa Rose", lisa);
		Map<String, Double> Gene = new HashMap<String, Double>();
		{
			// 'Gene Seymour': {'Lady in the Water': 3.0, 'Snakes on a Plane':
			// 3.5,
			// 'Just My Luck': 1.5, 'Superman Returns': 5.0, 'The Night
			// Listener':
			// 3.0,
			// 'You, Me and Dupree': 3.5},
			Gene.put("Lady in the Water", 3.0);
			Gene.put("Snakes on a Plane", 3.5);
			Gene.put("Just My Luck", 1.5);
			Gene.put("Superman Returns", 5.0);
			Gene.put("You, Me and Dupree", 3.5);
			Gene.put("The Night Listener", 3.0);
		}
		CRITICS.put("Gene Seymour", Gene);
		Map<String, Double> Michael = new HashMap<String, Double>();
		{
			// 'Michael Phillips': {'Lady in the Water': 2.5, 'Snakes on a
			// Plane':
			// 3.0,
			// 'Superman Returns': 3.5, 'The Night Listener': 4.0},
			Michael.put("Lady in the Water", 2.5);
			Michael.put("Snakes on a Plane", 3.0);
			Michael.put("Just My Luck", 1.5);
			Michael.put("Superman Returns", 3.5);
			Michael.put("The Night Listener", 4.0);
		}
		CRITICS.put("Michael Phillips", Michael);
		Map<String, Double> Claudia = new HashMap<String, Double>();
		{
			// 'Claudia Puig': {'Snakes on a Plane': 3.5, 'Just My Luck': 3.0,
			// 'The Night Listener': 4.5, 'Superman Returns': 4.0,
			// 'You, Me and Dupree': 2.5},
			Claudia.put("Snakes on a Plane", 3.5);
			Claudia.put("Just My Luck", 3.0);
			Claudia.put("Superman Returns", 4.0);
			Claudia.put("You, Me and Dupree", 2.5);
			Claudia.put("The Night Listener", 4.5);
		}
		CRITICS.put("Claudia Puig", Claudia);
		Map<String, Double> Mick = new HashMap<String, Double>();
		{
			// 'Mick LaSalle': {'Lady in the Water': 3.0, 'Snakes on a Plane':
			// 4.0,
			// 'Just My Luck': 2.0, 'Superman Returns': 3.0, 'The Night
			// Listener':
			// 3.0,
			// 'You, Me and Dupree': 2.0},
			Mick.put("Lady in the Water", 3.0);
			Mick.put("Snakes on a Plane", 4.0);
			Mick.put("Just My Luck", 2.0);
			Mick.put("Superman Returns", 3.0);
			Mick.put("You, Me and Dupree", 2.0);
			Mick.put("The Night Listener", 3.0);
		}
		CRITICS.put("Mick LaSalle", Mick);
		Map<String, Double> Jack = new HashMap<String, Double>();
		{

			// 'Jack Matthews': {'Lady in the Water': 3.0, 'Snakes on a Plane':
			// 4.0,
			// 'The Night Listener': 3.0, 'Superman Returns': 5.0, 'You, Me and
			// Dupree': 3.5},
			Jack.put("Lady in the Water", 3.0);
			Jack.put("Snakes on a Plane", 4.0);
			Jack.put("Just My Luck", 2.0);
			Jack.put("Superman Returns", 5.0);
			Jack.put("You, Me and Dupree", 3.5);
			Jack.put("The Night Listener", 3.0);
		}

		CRITICS.put("Jack Matthews", Jack);
		Map<String, Double> Toby = new HashMap<String, Double>();
		{
			// 'Toby': {'Snakes on a Plane':4.5,'You, Me and
			// Dupree':1.0,'Superman
			// Returns':4.0}}
			Toby.put("Snakes on a Plane", 4.5);
			Toby.put("You, Me and Dupree", 1.0);
			Toby.put("Superman Returns", 4.0);
		}
		CRITICS.put("Toby", Toby);
	}
}
