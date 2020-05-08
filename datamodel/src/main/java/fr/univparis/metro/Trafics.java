package fr.univparis.metro;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.function.BiPredicate;

/**
 * Allows the set up of trafics pertubations.
 */
public class Trafics {

  public enum Perturbation {
    LINE_SHUTDOWN,
    LINE_SLOW_DOWN,
    ENTIRE_STATION_SHUT_DOWN,
    PART_STATION_SHUT_DOWN,
    PART_LINE_SLOW_DOWN,
    PART_LINE_SHUT_DOWN,
    ALL_TRAFICS_SLOW_DOWN;
  }

  private static HashMap< String , WGraph<Station> > actualTrafics;
  private static HashMap< String , WGraph<Station> > initialTrafics;
  private static HashMap<String , HashMap<String , WGraph<Station>>> reverts;

 /**
  * Return the subway graph of a city which might have been modified with perturbation
  * @param city the city which we want the graph
  * @return the subway graph of the city
  */
  public static WGraph<Station> getGraph(String city) {
    return actualTrafics.get(city);
  }

 /**
  * Return the original subway graph of a city (without any perturbation)
  * @param city the city which we want the original graph
  * @return the original subway graph of the station
  */
  public static WGraph<Station> getInitialGraph(String city){
    return initialTrafics.get(city);
  }

 /**
  * Return a set containing all the names of the perturbations
  * @param city the city in which we want the perturbation
  * @return a set containing all the names of the perturbations
  */
  public static Set<String> getPerturbation(String city) {return reverts.get(city).keySet();}

 /**
  * Return a set of cities that are in the model
  * @return a set of the cities that are in the model
  */
  public static Set<String> getCities() {return actualTrafics.keySet();}

 /**
  * Initialize the trafics with files that are in Configuration
  * @see Configuration
  */
  public static void initTrafics() {
    actualTrafics = new HashMap< String , WGraph<Station> > ();
    initialTrafics = new HashMap< String , WGraph<Station> > ();

    reverts = new HashMap<String , HashMap<String , WGraph<Station>>> ();
    for (String city : Configuration.getCitiesName()) {
      InputStream i = Trafics.class.getResourceAsStream("/" + Configuration.getFileName(city));
      initialTrafics.put(city, Parser.loadFrom(i));
      actualTrafics.put(city, initialTrafics.get(city).clone());
      reverts.put(city, new HashMap<String, WGraph<Station>>());
    }
  }


  /**
  * Add a trafic perturbation
  * The revert graph of this perturbation will be stored in {@link #reverts}
  * @param city the city in which the perturbation occured
  * @param type the type of the perturbation
  * @param name the name of the perturbation
  * @param parameter is the paramerer of the perturbation (for example the name of the line not working)
  */
  public static void addPerturbation(String city, Perturbation type, String name, Object parameter) {
    if (! actualTrafics.containsKey(city)) throw new IllegalArgumentException();
    WGraph<Station> revert = null;
    switch (type) {
      case LINE_SHUTDOWN:
        if (! ( parameter instanceof String)) throw new IllegalArgumentException();
        revert = lineShutdown(city, (String) parameter);
        break;
      case LINE_SLOW_DOWN:
        if (! (parameter instanceof Pair<?, ?>)) throw new IllegalArgumentException();
        Pair<?,?> p1 = (Pair<?,?>) parameter;
        if (! (p1.getObj() instanceof String || ! (p1.getValue() instanceof Double) )) throw new IllegalArgumentException();
        revert = lineSlowDown(city, (String) p1.getObj(), (Double) p1.getValue());
        break;
      case ENTIRE_STATION_SHUT_DOWN:
        if (! (parameter instanceof String)) throw new IllegalArgumentException();
        revert = entireStationShutDown(city, (String) parameter);
        break;
      case PART_STATION_SHUT_DOWN:
        if (! (parameter instanceof Station)) throw new IllegalArgumentException();
        revert = partOfStationShutDown(city, (Station) parameter);
        break;
      case PART_LINE_SHUT_DOWN:
        if (! (parameter instanceof Pair<?, ?>)) throw new IllegalArgumentException();
        Pair<?, ?> p2 = (Pair<?, ?>) parameter;
        if (! (p2.getObj() instanceof Station) || ! (p2.getValue() instanceof Station)) throw new IllegalArgumentException();
        revert = partOfLineShutDown(city, (Station) p2.getObj(), (Station) p2.getValue());
        break;
      case PART_LINE_SLOW_DOWN:
        if (! (parameter instanceof Object[])) throw new IllegalArgumentException();
        Object[] objs = (Object[]) parameter;
        if (objs.length != 3) throw new IllegalArgumentException();
        if (! (objs[0] instanceof Station) || ! (objs[1] instanceof Station) || ! (objs[2] instanceof Double)) throw new IllegalArgumentException();
        revert = partOfLineSlowDown(city, (Station) objs[0], (Station) objs[1], (Double) objs[2]);
        break;
      case ALL_TRAFICS_SLOW_DOWN:
        if (! ( parameter instanceof Double)) throw new IllegalArgumentException();
	revert = allTraficsSlowDown(city, (Double) parameter);
	break;
    }
    if (revert != null) reverts.get(city).put(name, revert);
  }

  /**
  * Revert a perturbation
  * @param city the city in which the perturbation occured
  * @param name the name of the perturbation
  */
  public static void revertPerturbation(String city, String name) {
    if (! reverts.containsKey(city) || !reverts.get(city).containsKey(name)) {
      System.out.println(city);
      System.out.println(name);
      return;
    }
    actualTrafics.get(city).apply(reverts.get(city).get(name));
    reverts.get(city).remove(name);
  }


  /**
  * Modify the graph g so that we can't take a line
  * @param city the city in which we want to modify trafics
  * @param line the line we want to shutdown
  * @return a WGraph that we can use to revert this perturbation
  */
  public static WGraph<Station> lineShutdown(String city, String line) {
    WGraph<Station> actualG = actualTrafics.get(city);
    WGraph<Station> initialG = initialTrafics.get(city);
    WGraph<Station> revert = new WGraph<Station>();
    for (Station s : actualG.getVertices()) {
      if (! s.getLine().equals(line)) continue;
      if (! revert.containsVertex(s)) revert.addVertex(s);
      for (Station n : actualG.neighbors(s)) {
        if (! n.getLine().equals(line)) continue;
        if (! revert.containsVertex(n)) revert.addVertex(n);
        revert.addEdge(s, n, initialG.weight(s, n));
        actualG.setWeight(s, n, Double.POSITIVE_INFINITY);
      }
    }
    return revert;
  }

 /**
  * Modify the time between stations in a graph
  * @param city the city in which we want to modify trafics
  * @param line the line affected by the perturbation
  * @param times the time between every station of the line will be multipclated by it
  * @return a WGraph that can be use to revert this perturbation
  */
  public static WGraph<Station> lineSlowDown(String city, String line, double times) {
    WGraph<Station> actualG = actualTrafics.get(city);
    WGraph<Station> initialG = initialTrafics.get(city);
    WGraph<Station> revert = new WGraph<Station>();
    for (Station s : actualG.getVertices()) {
      if (! s.getLine().equals(line)) continue;
      if (! revert.containsVertex(s)) revert.addVertex(s);
      for (Station n : actualG.neighbors(s)) {
        if (! n.getLine().equals(line)) continue;
        if (! revert.containsVertex(n)) revert.addVertex(n);
        revert.addEdge(s, n, initialG.weight(s, n));
        actualG.setWeight(s, n, initialG.weight(s, n) * times);
      }
    }
    return revert;
  }

  /**
  * Modify the graph g so that we can't stop or start by a station
  * Passing over the station will still be possible
  * @param city the city in which we want to modify trafics
  * @param station the station we want to shutdown
  * @return a WGraph that we can use to revert this perturbation
  */
  public static WGraph<Station> entireStationShutDown(String city, String station) {
    WGraph<Station> actualG = actualTrafics.get(city);
    WGraph<Station> initialG = initialTrafics.get(city);
    WGraph<Station> revert = new WGraph<Station>();
    for (Station s : actualG.getVertices()) {
      if (! s.getName().equals(station)) continue;
      if (! revert.containsVertex(s)) revert.addVertex(s);
      for (Station n : actualG.neighbors(s)) {
        if (! n.getName().equals(station)) continue; // The line must be able to pass by the station
        if (! revert.containsVertex(n)) revert.addVertex(n);
        revert.addEdge(s, n, initialG.weight(s, n));
        actualG.setWeight(s, n, Double.POSITIVE_INFINITY);
      }
    }
    return revert;
  }

 /**
  * Modify the graph g so that we can't take one line of a station
  * @param city the city in which we want to modify trafics
  * @param st the station (name and line) we can't stop by
  * @return a WGraph that we can use to revert this perturbation
  */
  public static WGraph<Station> partOfStationShutDown(String city, Station st) {
    WGraph<Station> actualG = actualTrafics.get(city);
    WGraph<Station> initialG = initialTrafics.get(city);
    WGraph<Station> revert = new WGraph<Station>();
    for (Station s :actualG.getVertices()) {
      if (! s.getName().equals(st.getName())) continue;
      if (actualG.neighbors(s).contains(st)) {
        if (! revert.containsVertex(s)) revert.addVertex(s);
        revert.addEdge(s, st, initialG.weight(s, st));
        actualG.setWeight(s, st, Double.POSITIVE_INFINITY);
      }
    }
    for (Station n : actualG.neighbors(st)) {
      if (n.getLine().equals(st.getLine())) continue;
      if (! revert.containsVertex(n)) revert.addVertex(n);
      revert.addEdge(st, n, initialG.weight(st, n));
      actualG.setWeight(st, n, Double.POSITIVE_INFINITY);
    }
    return revert;
  }
 /**
  * Modify the graph g so we can't take a line from one station to an other one
  * @param city the city in which we want to modify trafics
  * @param start the start of the shutdown
  * @param end the end of the shutdown
  * @return a WGraph that we can use to revert this perturbation
  * Note that start and end stations must be one the same line
  */
  public static WGraph<Station> partOfLineShutDown(String city, Station start, Station end) {
    if (start.getLine() != end.getLine()  || start.getLine().startsWith("Meta Station")) return null;
    WGraph<Station> actualG = actualTrafics.get(city);
    WGraph<Station> initialG = initialTrafics.get(city);
    WGraph<Station> revert = new WGraph<Station>();
    HashMap<Pair<Station, Integer>, Pair<Station, Integer>> prev = new HashMap<>();
    HashMap<Pair<Station, Integer>, Double> dist = new HashMap<>();
    BiPredicate<Station, Station> sameLine = (Station s1, Station s2) -> s1.getLine().equals(s2.getLine()) || s1.getLine().startsWith("Meta Station") || s2.getLine().startsWith("Meta Station");
    BouarahAlgorithm.shortestPath(initialG, start, 0, sameLine, prev, dist);

    Station it = end;
    revert.addVertex(it);
    Station prec;
    while(true) {
      try {
        prec = prev.get(new Pair<Station, Integer>(it, 0)).getObj();
      } catch(NullPointerException e) {break;}
      revert.addVertex(prec);
      if (initialG.weight(it, prec) != Double.NaN) {
        revert.addEdge(it, prec, initialG.weight(it, prec));
        actualG.setWeight(it, prec, Double.POSITIVE_INFINITY);
      }
      revert.addEdge(prec, it, initialG.weight(prec, it));
      actualG.setWeight(prec, it, Double.POSITIVE_INFINITY);

      it = prec;
    }
    return revert;

  }
 /**
  * Modify the graph g so that a part of the line is slow down
  * @param city the city in which we want to add the perturbation
  * @param start the start of the slow down
  * @param end the end of the slow down
  * @param times the time for every traject concerned will be multiplied by it
  * @return a WGraph that can be use to revert this perturbation
  * Note that the start and end stations must be on the same line
  */
  public static WGraph<Station> partOfLineSlowDown(String city, Station start, Station end, Double times) {
    if (start.getLine() != end.getLine()  || start.getLine().startsWith("Meta Station")) return null;
    WGraph<Station> actualG = actualTrafics.get(city);
    WGraph<Station> initialG = initialTrafics.get(city);
    WGraph<Station> revert = new WGraph<Station>();
    HashMap<Pair<Station, Integer>, Pair<Station, Integer>> prev = new HashMap<>();
    HashMap<Pair<Station, Integer>, Double> dist = new HashMap<>();
    BiPredicate<Station, Station> sameLine = (Station s1, Station s2) -> s1.getLine().equals(s2.getLine()) || s1.getLine().startsWith("Meta Station") || s2.getLine().startsWith("Meta Station");
    BouarahAlgorithm.shortestPath(initialG, start, 0, sameLine, prev, dist);

    Station it = end;
    revert.addVertex(it);
    Station prec;
    while(true) {
      try {
        prec = prev.get(new Pair<Station, Integer>(it, 0)).getObj();
      } catch(NullPointerException e) {break;}
      revert.addVertex(prec);
      if (initialG.weight(it, prec) != Double.NaN) {
        revert.addEdge(it, prec, initialG.weight(it, prec));
        actualG.setWeight(it, prec, actualG.weight(it, prec) * times);
      }
      revert.addEdge(prec, it, initialG.weight(prec, it));
      actualG.setWeight(prec, it, actualG.weight(prec, it) * times);

      it = prec;
    }
    return revert;

  }

 /**
  * Modify the time between stations in a graph
  * @param city the city in which we want to modify trafics
  * @param times the time between every station will be multipclated by it
  * @return a WGraph that can be use to revert this perturbation
  */
  public static WGraph<Station> allTraficsSlowDown(String city, double times) {
    WGraph<Station> actualG = actualTrafics.get(city);
    WGraph<Station> initialG = initialTrafics.get(city);
    WGraph<Station> revert = new WGraph<Station>();
    for (Station s : actualG.getVertices()) {
      if (! revert.containsVertex(s)) revert.addVertex(s);
      for (Station n : actualG.neighbors(s)) {
        if (! revert.containsVertex(n)) revert.addVertex(n);
        revert.addEdge(s, n, initialG.weight(s, n));
	if( s.sameName(n) ) // pas de ralentissement sur les correspondances
	 actualG.setWeight(s, n, initialG.weight(s, n));
	else
	 actualG.setWeight(s, n, initialG.weight(s, n) * times);
      }
    }
    return revert;
  }

}
