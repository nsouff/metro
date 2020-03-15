package fr.univparis.metro;
import java.util.HashMap;
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


  public static <T> int minimumCorrespondance(WGraph<T> g, Predicate<T> start, Predicate<T> end, BiPredicate<T, T> p) {

    HashMap<Pair<T, Integer>, Pair<T, Integer>> prev = new HashMap<>();
    HashMap<Pair<T, Integer>, Double> dist = new HashMap<>();

    int limit = 0;
    Pair<T, Integer> pair;
    while (true) {
      for (T s : g.getVertices()) {
        if (! start.test(s)) continue;
        pair = new Pair<>(s, limit);

        BouarahAlgorithm.shortestPath(g, s, limit, p, prev, dist);
        for (T e : g.getVertices()) {
          pair = new Pair<>(e, limit);
          if (! end.test(e)) continue;
          if (dist.get(pair).equals(Double.POSITIVE_INFINITY)) {
            limit++;
            break;
          }
        }
      }
      return limit;
    }
  }
}
