package fr.univparis.metro;
import java.util.HashMap;
import java.util.*;

public class Dijkstra {

   /**
    * Add all the T element in dist and priQueue that are in g
    * The associated value for each of the element is 0 if it is equals to s, else positive infinity
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
    * Used to find the shortest path between a starting T and all other Ts of a graph
    * @param g    Graph of T
    * @param s    T where we begin our path
    * @param prev HashMap where for each T is associate the previous T for the shortest path since s
    * @param dist HashMap where for each T is associate his distance to s with the shortest path to s
    * @return a Pair composed of a HashMap of the previous T of each T and a HashMap with the minimal distance to the Ts from the parameter s
    */
    public static <T> void shortestPath (WGraph<T> g, T s, HashMap<T, T> prev, HashMap<T, Double> dist) {
      dist.clear();
      prev.clear();
      ArrayList<T> e = new ArrayList<T>();

      for (T st : g.getVertices()){
        e.add(st);
        if(s.equals(st))
          dist.put(st, 0.);
        else {
          dist.put(st, Double.POSITIVE_INFINITY);
        }
      }
      while (!e.isEmpty()){
        T u = minimalDistance(dist, e);
        if (u == null) {
          System.out.println(dist);
          System.out.println(e);
        }
        e.remove(u);
        List<T> v = g.neighbors(u);
        for (T st : v){
          if (dist.get(st) > dist.get(u) + g.weight(u, st)){
            dist.put(st, dist.get(u) + g.weight(u, st));
            prev.put(st, u);
          }
        }
      }

    }
    private static <T> T minimalDistance (HashMap<T, Double> d, ArrayList<T> e){
      Double distM=Double.POSITIVE_INFINITY;
      T a=null;
      for (T s : d.keySet()){
        if (distM>=d.get(s) && e.contains(s)){
          distM = d.get(s);
          a = s;
        }
      }
      return a;
    }

}
