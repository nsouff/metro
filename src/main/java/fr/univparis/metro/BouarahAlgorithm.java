package fr.univparis.metro;

import java.util.HashMap;
import java.lang.Integer;
import java.lang.Double;
import java.util.function.BiPredicate;

public class BouarahAlgorithm {

    /**
     * Initialize *dist* and *priQueue*
     * For each pair <V,k> where V is a vertex in *graph* and k is an integer between 0 and *limit*
     * <V,k> is added to *dist* with +infininty if V != *start* else 0.0
     * <V,k> is added into *priQueue* with priority dist[<V,k>]
     */
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

    
    /**
     * Find the shortest path with a
     * @param graph A weighted and oriented graph
     * @param start The start of research
     * @param limit The number of passage between equivalence classes allowed
     * @param equivalentRelation An equivalence relation i.e. a binary relation that is reflexive, symmetric and transitive 
     * @param prev A HashMap associating a pair <V,k> to the previous pair <W,i>. W is a neighbor of V and i=k iff W and V are in the same equivalence classes
     * @param dist A HashMap associating a pair <V,k> to his distance from start with a shortest path between <start,0> and <V,k>
     */
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
