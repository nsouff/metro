package fr.univparis.metro;
import java.util.*;

public class Dijkstra{

    /**
    * Find the T with the minimal distance in a list of T
    * @param d HashMap of the distance which exist yet
    * @param e ArrayList of the T which have not yet been evaluated in the HashMap default:
    * @return the T with the minimal distance and which is in a list of T
    */
    private static <T> T minimalDistance (HashMap<T, Double> d, ArrayList<T> e){
      Double distM=Double.POSITIVE_INFINITY;
      T a=null;
      for (T s : d.keySet()){
        if (distM>d.get(s) && e.contains(s)){
          distM = d.get(s);
          a = s;
        }
      }
      return a;
    }

    /**
    * Used to find the shortest path between a starting Station and all other stations of a graph
    * @param g    Graph of Station
    * @param s    Station where we begin our path
    * @param prev HashMap where for each Station is associate the previous Station for the shortest path since s
    * @param dist HashMap where for each Station is associate his distance to s with the shortest path to s
    * @return a Pair composed of a HashMap of the previous Station of each Station and a HashMap with the minimal distance to the stations from the parameter s
    */
    public static <T> Pair<T> shortestPath (WGraph<T> g, T s, HashMap<T, T> prev, HashMap<T, Double> dist){
      Pair<T> pair = new Pair<T>();
      ArrayList<T> e = new ArrayList<T>();
      prev = new HashMap<T, T>();
      dist = new HashMap<T, Double>();

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
        e.remove(u);
        List<T> v = g.neighbors(u);
        for (T st : v){
          if (dist.get(st) > dist.get(u) + g.weight(u, st)){
            dist.put(st, dist.get(u) + g.weight(u, st));
            prev.put(st, u);
          }
        }
      }
      pair = new Pair<T>(prev, dist);
      return pair; 
    } 

    static class Pair<T>{
      
      private HashMap<T, T> p;
      private HashMap<T, Double> d;

      public Pair (HashMap<T, T> a, HashMap<T, Double> b){
        p = a;
        d = b;
      }

      public Pair (){
        p = new HashMap<T, T>();
        d = new HashMap<T, Double>(); 
      }

      public HashMap<T, T> getP() {
        return p;
      }
      public HashMap<T, Double> getD() {
        return d;
      }
    } 
}

