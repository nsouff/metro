package fr.univparis.metro;

import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class Trafics {

  public enum Perturbation {
    LINE_SHUTDOWN,
    LINE_SLOW_DOWN,
    ENTIRE_STATION_SHUT_DOWN,
    PART_STATION_SHUT_DOWN;
  }

  private static HashMap< String , WGraph<Station> > actualTrafics;
  private static HashMap< String , WGraph<Station> > initialTrafics;
  private static HashMap<String , HashMap< Pair<Perturbation, String> , WGraph<Station>>> reverts;

  public static WGraph<Station> getGraph(String city) {
    return actualTrafics.get(city);
  }

  public static Set<Pair<Perturbation, String>> getPerturbation(String city) {return reverts.get(city).keySet();}

  public static Set<String> getCities() {return actualTrafics.keySet();}

  public static void initTrafics() throws IOException {
    /* FIXME: we are parsing twice every file, cloning might be quicker at runtime */
    actualTrafics = new HashMap< String , WGraph<Station> > ();
    initialTrafics = new HashMap< String , WGraph<Station> > ();

    reverts = new HashMap<String , HashMap< Pair<Perturbation, String> , WGraph<Station>>> ();
    for (String city : Configuration.getCitiesName()) {
      actualTrafics.put(city, Parser.loadFrom(new File(Configuration.getFileName(city))));
      initialTrafics.put(city, Parser.loadFrom(new File(Configuration.getFileName(city))));
      reverts.put(city, new HashMap<Pair<Perturbation, String>, WGraph<Station>>());
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
        Pair<?,?> p = (Pair<?,?>) parameter;
        if (! (p.getObj() instanceof String || ! (p.getValue() instanceof Double) )) throw new IllegalArgumentException();
        revert = lineSlowDown(city, (String) p.getObj(), (Double) p.getValue());
        break;
      case ENTIRE_STATION_SHUT_DOWN:
        if (! (parameter instanceof String)) throw new IllegalArgumentException();
        revert = entireStationShutDown(city, (String) parameter);
        break;
      case PART_STATION_SHUT_DOWN:
        if (! (parameter instanceof Station)) throw new IllegalArgumentException();
        revert = partOfStationShutDown(city, (Station) parameter);
        break;
    }
    if (revert != null) reverts.get(city).put(new Pair<Perturbation, String>(type, name), revert);
  }

  /**
  * Revert a perturbation
  * @param city the city in which the perturbation occured
  * @param type the type of the perturbation
  * @param name the name of the perturbation
  */
  public static void revertPerturbation(String city, Perturbation type, String name) {
    Pair<Perturbation, String> p = new Pair<Perturbation, String>(type, name);
    if (! reverts.containsKey(city) || !reverts.get(city).containsKey(p)) return;
    actualTrafics.get(city).apply(reverts.get(city).get(p));
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


}
