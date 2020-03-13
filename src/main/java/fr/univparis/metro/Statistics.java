package fr.univparis.metro;
import java.util.HashMap;
import java.util.function.Predicate;

public class Statistics{

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
    System.out.println("Les deux stations les plus éloignées du réseau\nStation de départ : "+res.getObj().getObj()+"\nStation d'arrivée : "+res.getObj().getValue()+"\nDurée : "+res.getValue());
    return res;
  }
}
