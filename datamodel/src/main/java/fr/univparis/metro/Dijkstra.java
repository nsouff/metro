package fr.univparis.metro;
import java.util.HashMap;
import java.util.*;

public class Dijkstra {

   /**
    * Add all the T element in dist and priQueue that are in g
    * The associated value for each of the element is 0 if it is equals to s, else positive infinity
    * Initialize the Hashmap of the distance between the initial T and the other T of the graph
    */
    private static <T> void initShortestPath(WGraph<T> g, T s, HashMap<T, Double> dist, PriorityQueue<T> priQueue) {
      for (T st : g.getVertices()){
        if(s.equals(st)) {
          priQueue.add(st, 0.);
          dist.put(st, 0.);
        }
        else {
          priQueue.add(st, Double.POSITIVE_INFINITY);
          dist.put(st, Double.POSITIVE_INFINITY);
        }
      }
    }

    /**
    * Used to find the shortest path between a starting T and all other T of a graph
    * @param g    Graph of T where we search a shortest path
    * @param s    T where we begin our path
    * @param prev HashMap where for each T is associate the previous T for the shortest path since s
    * @param dist HashMap where for each T is associate his distance to s with the shortest path to s
    */
    public static <T> void shortestPath (WGraph<T> g, T s, HashMap<T, T> prev, HashMap<T, Double> dist) {
      PriorityQueue<T> priQueue = new PriorityQueue<T>();
      dist.clear();
      prev.clear();
      initShortestPath(g, s, dist, priQueue);

      while (!priQueue.isEmpty()){
        T u = priQueue.poll();
        Set<T> v = g.neighbors(u);
        Double d;
        for (T st : v) {
          d = dist.get(u) + g.weight(u, st);
          if (dist.get(st) > d){
            dist.put(st, d);
            priQueue.updatePriority(st, d);
            prev.put(st, u);
          }
        }
      }
    }

}
