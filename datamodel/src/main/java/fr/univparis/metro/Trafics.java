package fr.univparis.metro;

import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.function.BiPredicate;

public class Trafics {

  public enum Perturbation {
    LINE_SHUTDOWN,
    LINE_SLOW_DOWN,
    ENTIRE_STATION_SHUT_DOWN,
    PART_STATION_SHUT_DOWN,
    PART_LINE_SLOW_DOWN,
    PART_LINE_SHUT_DOWN;
  }

  private static HashMap< String , WGraph<Station> > actualTrafics;
  private static HashMap< String , WGraph<Station> > initialTrafics;
  private static HashMap<String , HashMap<String , WGraph<Station>>> reverts;

  public static WGraph<Station> getGraph(String city) {
    return actualTrafics.get(city);
  }

  public static WGraph<Station> getInitialGraph(String city){
    return initialTrafics.get(city);
  }

  public static Set<String> getPerturbation(String city) {return reverts.get(city).keySet();}

  public static Set<String> getCities() {return actualTrafics.keySet();}

  public static void initTrafics() throws IOException {
    /* FIXME: we are parsing twice every file, cloning might be quicker at runtime */
    actualTrafics = new HashMap< String , WGraph<Station> > ();
    initialTrafics = new HashMap< String , WGraph<Station> > ();

    reverts = new HashMap<String , HashMap<String , WGraph<Station>>> ();
    for (String city : Configuration.getCitiesName()) {
      actualTrafics.put(city, Parser.loadFrom(new File(Configuration.getFileName(city))));
      initialTrafics.put(city, Parser.loadFrom(new File(Configuration.getFileName(city))));
      reverts.put(city, new HashMap<String, WGraph<Station>>());
    }
  }


  /**
  * Add a trafic perturbation
  * The revert graph of this perturbation will be stored in reverts -> city -> (type, name)
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

        revert = null;
        break;
    }
    if (revert != null) reverts.get(city).put(name, revert);
  }

  /**
  * Revert a perturbation
  * @param city the city in which the perturbation occured
  * @param type the type of the perturbation
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
  * Note that start and end must be one the same line
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


}
