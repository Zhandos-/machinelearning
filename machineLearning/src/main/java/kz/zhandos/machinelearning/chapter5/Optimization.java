package kz.zhandos.machinelearning.chapter5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import kz.zhandos.machinelearning.chapter3.Pair;

public class Optimization {
  private final static Logger log = Logger.getLogger(Optimization.class);
  public static final Map<Pair<String, String>, Flight> FLIGHTS =
      new HashMap<Pair<String, String>, Flight>();

  // people = [('Seymour','BOS'),
  // ('Franny','DAL'),
  // ('Zooey','CAK'),
  // ('Walt','MIA'),
  // ('Buddy','ORD'),
  // ('Les','OMA')]
  // # Laguardia

  public static List<Pair<String, String>> people = new ArrayList<Pair<String, String>>();

  static {
    people.add(Pair.pair("Seymour", "BOS"));
    people.add(Pair.pair("Franny", "DAL"));
    people.add(Pair.pair("Zooey", "CAK"));
    people.add(Pair.pair("Walt", "MIA"));
    people.add(Pair.pair("Buddy", "ORD"));
    people.add(Pair.pair("Les", "OMA"));
  }

  public static String destination = "LGA";


  public void readFile() {
    Scanner s = new Scanner(Optimization.class.getResourceAsStream("schedule.txt"));
    try {
      while (s.hasNext()) {
        String line = s.nextLine();
        String l[] = line.split(",");
        Flight flight = new Flight();
        flight.origin = l[0];
        flight.destination = l[1];
        flight.depart = l[2];
        flight.arrive = l[3];
        flight.price = Double.parseDouble(l[4]);
        FLIGHTS.put(Pair.pair(flight.origin, flight.destination), flight);
      }

    } catch (Exception e) {

    } finally {
      if (s != null)
        s.close();
    }
  }

  public Integer getMinutes(String s) {
    if (s == null)
      return null;
    String time[] = s.split(":");
    return Integer.parseInt(time[0]) + Integer.parseInt(time[1]);
  }


  public void printDestination(List<Long> r) {
    // def printschedule(r):
    // for d in range(len(r)/2):
    // name=people[d][0]
    // origin=people[d][1]
    // out=flights[(origin,destination)][r[d]]
    // ret=flights[(destination,origin)][r[d+1]]
    // print '%10s%10s %5s-%5s $%3s %5s-%5s $%3s' % (name,origin,
    // out[0],out[1],out[2],
    // ret[0],ret[1],ret[2])
    for (int d = 0; d < r.size() / 2; d++) {
      Pair<String, String> pair = people.get(r.get(d).intValue());
      String name = pair.first;
      String origin = pair.second;
      Flight out = FLIGHTS.get(Pair.pair(origin, destination));
      Flight ret = FLIGHTS.get(Pair.pair(destination, origin));
      log.info(String.format("%10s%10s %5s-%5s $%3s %5s-%5s $%3s", name, origin, out.arrive,
          out.depart, out.price, ret.arrive, ret.depart, ret.price));
    }



  }


  public int scheduleCost(List<Long> sol) {
    // def schedulecost(sol):
    int totalprice = 0;
    int latestarrival = 0;
    int earliestdep = 24 * 60;

    for (Long d : sol.subList(0, sol.size() / 2)) {
      // # Get the inbound and outbound flights
      String origin = people.get(d.intValue()).second;
      Flight outbound = FLIGHTS.get(Pair.pair(origin, destination));
      Flight returnf = FLIGHTS.get(Pair.pair(destination, origin));

      totalprice += outbound.price + returnf.price;
      latestarrival = Math.max(latestarrival, getMinutes(outbound.arrive));
      earliestdep = Math.max(earliestdep, getMinutes(returnf.depart));
    }
    // # Every person must wait at the airport until the latest person arrives.
    // # They also must arrive at the same time and wait for their flights.
    int totalwait = 0;
    for (Long d : sol.subList(0, sol.size() / 2)) {
      String origin = people.get(d.intValue()).second;
      Flight outbound = FLIGHTS.get(Pair.pair(origin, destination));
      Flight returnf = FLIGHTS.get(Pair.pair(destination, origin));
      totalwait += latestarrival - getMinutes(outbound.arrive);
      totalwait += getMinutes(returnf.depart) - earliestdep;
    }

    // # Does this solution require an extra day of car rental? That'll be $50!
    if (latestarrival > earliestdep)
      totalprice += 50;


    return totalprice + totalwait;
  }


  public static void main(String args[]) {
    Optimization optimization = new Optimization();
    optimization.readFile();
    List<Long> s = new ArrayList<Long>();
    s.addAll(Arrays.asList(1l, 4l, 3l, 2l, 3l, 6l, 3l, 2l, 4l, 5l, 3l));
    System.out.println(optimization.scheduleCost(s));
  }



}
