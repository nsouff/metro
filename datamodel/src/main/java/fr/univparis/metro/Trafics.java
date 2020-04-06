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

  private static HashMap< String , WGraph<Station> > trafics;
  private static HashMap<String , HashMap< Pair<Perturbation, String> , WGraph<Station>>> reverts;

  public static WGraph<Station> getGraph(String city) {
    return trafics.get(city);
  }

  public static Set<Pair<Perturbation, String>> getPertubation(String city) {return reverts.get(city).keySet();}

  public static Set<String> getCities() {return trafics.keySet();}

  public static void initTrafics() throws IOException {
    trafics = new HashMap< String , WGraph<Station> > ();
    reverts = new HashMap<String , HashMap< Pair<Perturbation, String> , WGraph<Station>>> ();
    for (String city : Configuration.getCitiesName()) {
      trafics.put(city, Parser.loadFrom(new File(Configuration.getFileName(city))));
      reverts.put(city, new HashMap<Pair<Perturbation, String>, WGraph<Station>>());
    }
  }


  /**
  * Add a trafic perturbation
  * The revert graph of this perturbation will be stored in reverts -> city -> (type, name)
  * @param city the city in which the perturbation occured
  * @param type the type of the perturbation
  * @param name the name of the perturbation
  * @param parameter is the paramerer of the pertubation (for example the name of the line not working)
  */
  public static void addPertubation(String city, Perturbation type, String name, Object parameter) {
    if (! trafics.containsKey(city)) throw new IllegalArgumentException();
    WGraph<Station> revert = null;
    switch (type) {
      case LINE_SHUTDOWN:
        if (! ( parameter instanceof String)) throw new IllegalArgumentException();
        revert = lineShutdown(trafics.get(city), (String) parameter);
        break;
      case LINE_SLOW_DOWN:
        break;
      case ENTIRE_STATION_SHUT_DOWN:
        if (! (parameter instanceof String)) throw new IllegalArgumentException();
        revert = entireStationShutDown(trafics.get(city), (String) parameter);
        break;
      case PART_STATION_SHUT_DOWN:
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
  public static void revertPertubation(String city, Perturbation type, String name) {
    Pair<Perturbation, String> p = new Pair<Perturbation, String>(type, name);
    if (! reverts.containsKey(city) || !reverts.get(city).containsKey(p)) return;
    trafics.get(city).apply(reverts.get(city).get(p));
  }


  /**
  * Modify the graph g so that we can't take a line
  * @param g is the graph we want to modify
  * @param line is the line we want to shutdown
  * @return a WGraph that we can use to revert this perturbation
  */
  public static WGraph<Station> lineShutdown(WGraph<Station> g, String line) {
    WGraph<Station> revert = new WGraph<Station>();
    for (Station s : g.getVertices()) {
      if (! s.getLine().equals(line)) continue;
      if (! revert.containsVertex(s)) revert.addVertex(s);
      for (Station n : g.neighbors(s)) {
        if (! n.getLine().equals(line)) continue;
        if (! revert.containsVertex(n)) revert.addVertex(n);
        revert.addEdge(s, n, g.weight(s, n));
        g.setWeight(s, n, Double.POSITIVE_INFINITY);
      }
    }
    return revert;
  }

  public static WGraph<Station> entireStationShutDown(WGraph<Station> g, String station) {
    WGraph<Station> revert = new WGraph<Station>();
    for (Station s : g.getVertices()) {
      if (! s.getName().equals(station)) continue;
      if (! revert.containsVertex(s)) revert.addVertex(s);
      for (Station n : g.neighbors(s)) {
        if (! n.getName().equals(station)) continue; // The line must be able to pass by the station
        if (! revert.containsVertex(n)) revert.addVertex(n);
        System.out.println(g.weight(s, n));
        revert.addEdge(s, n, g.weight(s, n));
        g.setWeight(s, n, Double.POSITIVE_INFINITY);
      }
    }
    return revert;
  }


}
