package fr.univparis.metro;

import java.util.HashMap;
import java.lang.Integer;
import java.lang.Double;
import java.util.function.BiPredicate;

public class BouarahAlgorithm {

    private static <T> void initShortestPath(WGraph<T> graph, T start, int limit, HashMap<Pair<T, Integer>, Double> dist, PriorityQueue<Pair<T,Integer>> priQueue) {
	Pair<T, Integer> p;
	for ( T vertex : graph.getVertices() ) {
	    if( start.equals(vertex) ) {		
		p = new Pair<T, Integer>(vertex, 0);
		priQueue.add(p, 0.0);
		dist.put(p, 0.0);
		for(int i=1; i <= limit; i++) {
		    p = new Pair<T, Integer>(vertex, i);
		    priQueue.add(p, Double.POSITIVE_INFINITY);
		    dist.put(p, Double.POSITIVE_INFINITY);
		}
	    }
	    else {		
		for(int i=0; i <= limit; i++) {
		    p = new Pair<T, Integer>(vertex, i);
		    priQueue.add(p, Double.POSITIVE_INFINITY);
		    dist.put(p, Double.POSITIVE_INFINITY);
		}
	    }
	}
    }

    

    public static <T> void shortestPath (WGraph<T> graph, T start, int limit, BiPredicate<T,T> equivalenceRelation,
					 HashMap<Pair<T, Integer>, Pair<T, Integer>> prev, HashMap<Pair<T, Integer>, Double> dist) {
	PriorityQueue<Pair<T, Integer>> priQueue = new PriorityQueue<Pair<T, Integer>>();
	prev.clear();
	dist.clear();

	initShortestPath(graph, start, limit, dist, priQueue);

	while ( !priQueue.isEmpty() ){
	    Pair<T, Integer> node = priQueue.poll();

	    for ( T n : graph.neighbors(node.getObj()) ) {
		int separation = node.getValue();
		if( !equivalenceRelation.test(node.getObj(), n)) // on vérifie l'appartenance à la même classe d'équivalence entre la parent et le fils
		    separation++;

		if( separation > limit )
		    continue;

		double d = dist.get(node) + graph.weight(node.getObj(), n); // distance de st à son parent + depuis l'origine
		Pair<T, Integer> child = new Pair<T, Integer>(n, separation); // le noeud correspondant
		if( dist.get(child) > d ) {
		    dist.put(child, d);
		    priQueue.updatePriority(child, d);
		    prev.put(child, node);
		}		
	    }
	}
    }
}
