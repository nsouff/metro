package fr.univparis.metro;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.BiPredicate;

/**
* Class for statistics about our networks
*
*/

public class Statistics{

  /**
  * This function give the two stations the more distant on a network
  * @param g the graph representing the network
  * @param p1 is the predicate to applicate Dijkstra to an element of the graph
  * @param p2 is the predicate to comparate the weight between two elements of the graph
  * @return a pair of pair of elements and the weight between these two elements which are the two elements the more distant on the graph
  */
  public static <T> Pair<Pair<T, T>, Double> mostDistantStations(WGraph<T> g, Predicate<T> p1, Predicate<T> p2){
    Double biggestTime = 0.;
    Pair<T, T> stations = new Pair<T,T>(null, null);
    Pair<Pair<T, T>, Double> res;
    HashMap<T, T> prev = new HashMap<T, T>();
    HashMap<T, Double> dist = new HashMap<T, Double>();
    for( T s : g.getVertices()){
      if(p1.test(s))continue;
      Dijkstra.shortestPath(g, s, prev, dist);
      for(T t : g.getVertices()){
        if(p2.test(t) && dist.get(t) > biggestTime){
          biggestTime = dist.get(t);
          stations = new Pair<T, T>(s, t);
        }
      }
      prev.clear();
      dist.clear();
    }
    res = new Pair<Pair<T,T>, Double>(stations, biggestTime);
    return res;
  }

  /**
  * Return the minimum correspondence to go from any start vertex to any end vertex
  * @param g the Graph
  * @param start indicates wich vertex are start vertex
  * @param end indicates wich vertex are end vertex
  * @param p indicates what is consider as a correspondence
  * @return the number of minimum correspondence
  */
  public static <T> int minimumCorrespondence(WGraph<T> g, Predicate<T> start, Predicate<T> end, BiPredicate<T, T> p) {

    HashMap<Pair<T, Integer>, Pair<T, Integer>> prev = new HashMap<>();
    HashMap<Pair<T, Integer>, Double> dist = new HashMap<>();

    int limit = 0;
    Pair<T, Integer> pair;
    while (true) {
      for (T s : g.getVertices()) {
        if (! start.test(s)) continue;

        BouarahAlgorithm.shortestPath(g, s, limit, p, prev, dist);

        for (T e : g.getVertices()) {
          if (! end.test(e)) continue;
          boolean pathExist = false;
          for(int i=0; i <= limit; i++) {
            pair = new Pair<>(e, i);
            if ( !dist.get(pair).equals(Double.POSITIVE_INFINITY)) {
              pathExist = true;
              break;
            }
          }
          if( !pathExist) {
            limit++;
            break;
          }
        }
      }
      return limit;
    }
  }

  /**
   * Return the line that has the more/less Station in it (excluding the Meta Line)
   * @param g represent the subway network
   * @param max indicates if we want the line with the most station (max == true) or the line with the less (max  == false) station
   * @return the name of the line with the most/less station in it
   */
  public static String extremumLine(WGraph<Station> g, boolean max) {
    HashMap<String, Integer> nbOfStation = new HashMap<String, Integer>();
    for (Station st : g.getVertices()) {
      if (st.getLine().startsWith("Meta Station")) continue;
      if (nbOfStation.containsKey(st.getLine())) nbOfStation.put(st.getLine(), nbOfStation.get(st.getLine()) + 1);
      else nbOfStation.put(st.getLine(), 1);
    }
    int extremum = (max) ? -1 : Integer.MAX_VALUE;
    String res = null;
    for (String line : nbOfStation.keySet()) {
      int nb = nbOfStation.get(line);
      if (max && nb > extremum) {
        res = line;
        extremum = nb;
      }
      else if (!max && nb < extremum) {
        res = line;
        extremum = nb;
      }
    }
    return res;
  }

  public static int averageNbOfStationPerLine(WGraph<Station> g){
    HashMap<String, MatriceWGraph> h = MatriceWGraph.initializeAllLineGraphs(g);
    int nbStation = 0;
    int nbLine = 0;
    for(String stationName : h.keySet()){
      nbLine++;
      nbStation += h.get(stationName).getDirect().length;
    }
    return (int) nbStation/nbLine;
  }
}
