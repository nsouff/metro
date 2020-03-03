package fr.univparis.metro;

import java.util.HashMap;
import java.util.List;
import java.lang.Integer;
import java.lang.Double;
import java.util.Map;
import java.util.function.BiPredicate;

public class BouarahAlgorithm {

    private static void initShortestPath(WGraph<Station> g, Station s, int limit, HashMap<Pair<Station, Integer>, Double> dist, PriorityQueue<Pair<Station,Integer>> priQueue) {
	for ( Station st : g.getVertices() ) {
	    if( s.equals(st) ) {
		Pair<Station, Integer> root = new Pair<>(st, 0);
		priQueue.add(root, 0.0);
		dist.put(root, 0.0);
		for(int i=1; i <= limit; i++) {
		    root = new Pair<>(st, i);
		    priQueue.add(root, Double.POSITIVE_INFINITY);
		    dist.put(root, Double.POSITIVE_INFINITY);
		}
	    }
	    else {		
		for(int i=0; i <= limit; i++) {
		    Pair<Station, Integer> p = new Pair<>(st, i);
		    priQueue.add(p, Double.POSITIVE_INFINITY);
		    dist.put(p, Double.POSITIVE_INFINITY);
		}
	    }
	}
    }

    public static void shortestPath (WGraph<Station> g, Station s, int limit, BiPredicate<Station,Station> equivalenceRelation,
				     HashMap<Pair<Station, Integer>, Pair<Station, Integer>> prev, HashMap<Pair<Station, Integer>, Double> dist) {
	PriorityQueue<Pair<Station, Integer>> frontiere = new PriorityQueue<>();
	prev.clear();
	dist.clear();

	initShortestPath(g, s, limit, dist, frontiere);

	while ( !frontiere.isEmpty() ){
	    Pair<Station, Integer> node = frontiere.poll();

	    for ( Station st : g.neighbors(node.getObj()) ) {
		int separation = node.getValue();
		if( !equivalenceRelation.test(node.getObj(), st)) // on vérifie l'appartenance à la même classe d'équivalence entre la parent et le fils
		    separation++;

		if( separation > limit )
		    continue;

		double d = dist.get(node) + g.weight(node.getObj(), st); // distance de st à son parent + depuis l'origine
		Pair<Station, Integer> child = new Pair<Station, Integer>(st, separation); // le noeud correspondant
		if( dist.get(child) > d ) {
		    dist.put(child, d);
		    frontiere.updatePriority(child, d);
		    prev.put(child, node);
		}		
	    }
	}
    }
}
