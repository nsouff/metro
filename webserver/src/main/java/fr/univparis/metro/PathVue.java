package fr.univparis.metro;

import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiPredicate;
/**
 * Class used to get an itinerary as an html String which will be used in the webserver
 */
public class PathVue {
 /**
  * Return a description of a time in hour, minutes and seconds
  * @param time the time (in second) we want a desciption
  * @return a description of a time in hour, minutes and seconds
  */
  public static String time(Double time) {
    if (time == Double.POSITIVE_INFINITY) throw new IllegalArgumentException();
    Double[] times = WebserverLib.doubleToTime(time);
    return "Average time to get to your destination : " + times[0] + " h, "  + times[1] + " min, " + times[2] + " s.";
  }

  ////////////////////////////////////////
  // limited connection path with Floyd //
  ////////////////////////////////////////

  private static String pathFloyd(ArrayList<Pair<String,String>> l, String from, String to){
    String ret = "Departure : " + from + "<br><br>";
    int i = 1;
    for(Pair<String, String> p : l){
      String str = p.getObj();
      if(p.getObj().contains("$")){
        str = "";
        for(int j = 0; j < p.getObj().length(); j++){
          if(p.getObj().charAt(j) == '$') break;
          str += p.getObj().charAt(j);
        }
      }
      if(i == l.size() - 1){
        ret += "line : " + p.getValue() + " : " + str + " -> " + l.get(i).getObj() + "<br><br>";
        ret += "Arrival : " + to;
        break;
      }
      ret += "line : " + p.getValue() + " : " + str + " -> " + l.get(i).getObj() + "<br>";
      i++;
    }
    return ret;
  }

 /**
  * Return a description of an itinerary and its time using floyd algorithm
  * @param g the graph representing the whole subway network
  * @param from the start staion of the itinerary
  * @param to the destination of the itinerary
  * @return a description of an itinerary and its time using floyd algorithm
  */
  public static String limitedConnexionPathWithFloyd(WGraph<Station> g, Station from, Station to){
    String start = from.getName();
    String end = to.getName();
    String body = "";
    HashMap<String, MatriceWGraph> lines = MatriceWGraph.initializeAllLineGraphs(g);
    MatriceWGraph matriceGraph = new MatriceWGraph(g, lines);
    LimitedConnectionSearch.floyd(matriceGraph.getDirect(), matriceGraph.getVia(), matriceGraph.getIntermediate());
    ArrayList<Pair<String, String>> l = LimitedConnectionSearch.getPath(matriceGraph, start, end);
    String str1 = l.get(0).getObj();
    Collections.reverse(l);
    String str2 = l.get(0).getObj();
    Double t = matriceGraph.getDirect()[matriceGraph.getSetOfVertices().get(str2)][matriceGraph.getSetOfVertices().get(str1)];
    t += (matriceGraph.getIntermediate()[matriceGraph.getSetOfVertices().get(str2)][matriceGraph.getSetOfVertices().get(str1)] - 1) * Parser.defaultChangeStationWeight;
    body = "<h2>Time</h2>\n" + time(t) + "\n" + "<h2>Itinerary</h2>\n" + pathFloyd(l, start, end);
    return body;
  }

  /**
  * @param prev a HashMap acting like a tree. the root has null parent
  * @param target The targetted node
  * @param <T> a class that could represent one step of an itinerary
  * @return A list of nodes going from the root of prev to target
  */
  public static <T> LinkedList<T> buildPath(HashMap<T, T> prev, T target) {
    LinkedList<T> path = new LinkedList<T>();
    T current = target;
    while( current != null ) {
      path.addFirst(current);
      current = prev.get(current);
    }
    return path;
  }


  private static String path(HashMap<Pair<Station, Integer>,Pair<Station, Integer>> prev, Pair<Station, Integer> to) {
    LinkedList<Pair<Station, Integer>> path = buildPath(prev, to);

    path.removeFirst(); // on enlève la meta station start...
    path.removeLast();  // ...et la meta station end

    String from = path.getFirst().getObj().getName();
    String line = path.getFirst().getObj().getLine();
    String res = "Departure : " + from + "<br><br>"+"line " + line + " : " + from + " -> ";
    for (Pair<Station, Integer> st : path) {
      if (!st.getObj().getLine().equals(line)) {
        res += st.getObj().getName() + "<br>" + "line " + st.getObj().getLine() + " : " + st.getObj().getName() + " -> ";
        line = st.getObj().getLine();
      }
    }
    res += to.getObj().getName() + "<br><br>Arrival: " + to.getObj().getName();
    return res;
  }


  ///////////////////////////////////////////////////
  // limited connection path with BouarahAlgorithm //
  ///////////////////////////////////////////////////

  private static <T> boolean isThereAnyPath(HashMap<T, Double> dist, T to) {
    return dist.containsKey(to) && !dist.get(to).equals(Double.POSITIVE_INFINITY);
  }

 /**
  * Return a description of an itinerary and its time using Bouarah algorithm
  * @param g the graph representing the subway
  * @param start the start sttaion of the itinerary
  * @param to the destination of the itinerary
  * @return a description of an itinerary and its time using Bouarah algorithm
  */
  public static String limitedConnectionPath(WGraph<Station> g, Station start, Station to) {
    // on lance Dijkstra pour vérifier l'existence du chemin
    HashMap<Station, Station> prev = new HashMap<Station, Station>();
    HashMap<Station, Double> dist = new HashMap<Station, Double>();
    Dijkstra.shortestPath(g, start, prev, dist);
    if( !isThereAnyPath(dist,to) ) {
      return "Due to actual trafics perturbation we couldn't find any path from " + start.getName() + " to " + to.getName();
    }

    // on lance ensuite BouarahAlgorithm pour trouver un chemin en un nombre minimal de correspondances
    HashMap<Pair<Station, Integer>, Pair<Station, Integer>> prevLimited = new HashMap<Pair<Station, Integer>, Pair<Station, Integer>>();
    HashMap<Pair<Station, Integer>, Double> distLimited = new HashMap<Pair<Station, Integer>, Double>();
    BiPredicate<Station, Station> sameLine = (Station s1, Station s2) -> s1.getLine().equals(s2.getLine()) || s1.getLine().startsWith("Meta Station") || s2.getLine().startsWith("Meta Station");
    int limit = 0;
    Pair<Station, Integer> toLimited = new Pair<Station, Integer>(to, limit);

    while( true ) { // le chemin existe
      BouarahAlgorithm.shortestPath(g, start, limit, sameLine, prevLimited, distLimited);
      if( isThereAnyPath(distLimited, toLimited) )
      break;
      limit += 1;
      toLimited.setValue(limit);
    }
    String time = time(distLimited.get(toLimited));
    String itinerary = path(prevLimited, toLimited);
    return "<h2>Time</h2>\n" + time + "\n" + "<h2>Itinerary</h2>\n" + itinerary;
  }


  /////////////////////////////////
  // multiple path with Dijkstra //
  /////////////////////////////////

  private static String path(HashMap<Station, Station> prev, Station to, ArrayList<Pair<Station, Station>> changingStation) {
    changingStation.clear();

    LinkedList<Station> path = buildPath(prev, to);

    path.removeFirst(); // on enlève la meta station start...
    path.removeLast();  // ...et la meta station end

    String from = path.getFirst().getName();
    String line = path.getFirst().getLine();
    String res = "Departure : " + from + "<br><br>"+"line " + line + " : " + from + " -> ";
    Station prec = null;
    for (Station st : path) {
      if (!st.getLine().equals(line)) {
        changingStation.add(new Pair<Station, Station>(prec, st));
        res += st.getName() + "<br>" + "line " + st.getLine() + " : " + st.getName() + " -> ";
        line = st.getLine();
      }
      prec = st;
    }
    res += to.getName() + "<br><br>Arrival: " + to.getName();
    return res;
  }

 /**
  * Return descriptions of itineraries using Dijkstra multiple times
  * @param g the graph representing the subway
  * @param start the start sttaion of the itineraries
  * @param to the destination of the itineraries
  * @return descriptions of itineraries
  */
  public static String multiplePath(WGraph<Station> g, Station start, Station to) {
    TreeSet<Pair<String, Double>> resAux = new TreeSet<Pair<String, Double>>((p1, p2) -> {
      if (p1.getValue() < p2.getValue()) return -1;
      if (p1.getValue() > p2.getValue()) return 1;
      else {
        return p1.getObj().hashCode() - p2.getObj().hashCode();
      }
    });
    double THRESHOLD = 1.2;
    HashMap<Station, Station> prev = new HashMap<Station, Station>();
    HashMap<Station, Double> dist = new HashMap<Station, Double>();
    ArrayList<Pair<Station, Station>> changingStation  = new ArrayList<Pair<Station, Station>>();
    Dijkstra.shortestPath(g, start, prev, dist);
    try {
      resAux.add(new Pair<String, Double>(
      "<h2>Time</h2>\n" +
      time(dist.get(to)) +
      "<h2>Itinerary</h2>" +
      path(prev, to, changingStation),
      dist.get(to)
      ));

    } catch(RuntimeException e) {
      return "Due to actual trafics perturbation we couldn't find any path from " + start.getName() + " to " + to.getName();
    }

    multiplePathAux(g, start, to, changingStation, resAux, THRESHOLD * dist.get(to), 0);

    String res = "";
    for (Pair<String, Double> p : resAux) {
      res += p.getObj();
    }
    return res;
  }

  private static void multiplePathAux(WGraph<Station> g, Station start, Station to, ArrayList<Pair<Station, Station>> changingStation, TreeSet<Pair<String, Double>> resAux, Double threshold, int depth) {
    int MAX_CORRESPONDANCES = 3;
    int MAX_DEPTH = 5;
    if (depth >= MAX_DEPTH) return;

    HashMap<Station, Station> prev = new HashMap<Station, Station>();
    HashMap<Station, Double> dist = new HashMap<Station, Double>();
    for (Pair<Station, Station> p : changingStation) {
      WGraph<Station> revert = new WGraph<Station>();
      Station st = p.getObj();
      revert.addVertex(st);
      for (Station n : g.neighbors(st)) {
        if (n.getName().equals(st.getName()) && ! n.getLine().startsWith("Meta Station")) {
          // Changin are on both sides
          revert.addVertex(n);
          revert.addEdge(st, n, g.weight(st, n));
          g.setWeight(st, n, Double.POSITIVE_INFINITY);
        }
      }
      ArrayList<Pair<Station, Station>> pChangingStation = new ArrayList<Pair<Station, Station>>();
      Dijkstra.shortestPath(g, start, prev, dist);
      if (dist.get(to) <= threshold) {
        String itinerary = path(prev, to, pChangingStation);
        if (pChangingStation.size() <= MAX_CORRESPONDANCES) {
          resAux.add(new Pair<String, Double>(
          "<h2>Time</h2>\n" +
          time(dist.get(to)) +
          "<h2>Itinerary</h2>" +
          itinerary,
          dist.get(to)
          ));
          multiplePathAux(g, start, to, pChangingStation, resAux, threshold, depth + 1);
        }

      }
      g.apply(revert);
    }
  }

}
