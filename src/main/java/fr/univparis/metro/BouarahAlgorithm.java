package fr.univparis.metro;

import java.util.HashMap;
import java.util.List;
import java.lang.Integer;
import java.lang.Double;
import java.util.Map;
import java.util.function.BiPredicate;

public class BouarahAlgorithm {

    /**
     * Used to find the shortest path between a starting T and all other Ts of a graph
     * @param g    Graph of T
     * @param s    T where we begin our path
     * @param prev HashMap where for each T is associate the previous T for the shortest path since s
     * @param dist HashMap where for each T is associate his distance to s with the shortest path to s
     * @return a Pair composed of a HashMap of the previous T of each T and a HashMap with the minimal distance to the Ts from the parameter s
     */
    public static void shortestPath (WGraph<Station> g, Station s, int limit, BiPredicate<Station,Station> equivalenceRelation) {
	PriorityQueue<Pair<Station, Integer>> frontiere = new PriorityQueue<>();
	HashMap<Pair<Station, Integer>, Pair<Station, Integer>> pred = new HashMap<>();
	HashMap<Pair<Station, Integer>, Double> dist = new HashMap<>();

	Pair<Station, Integer> root = new Pair<Station, Integer>(s, 0);
	frontiere.add(root, 0.0);
	pred.put(root, null);
	dist.put(root, 0.0);

	while ( !frontiere.isEmpty() ){
	    Pair<Station, Integer> node = frontiere.poll();

	    for ( Station st : g.neighbors(node.getObj()) ) {
		int separation = node.getValue();
		if( !equivalenceRelation.test(node.getObj(), st)) // on vérifie l'appartenance à la même classe d'équivalence
		    separation++;

		if( separation > limit )
		    continue;

		Pair<Station, Integer> child = new Pair<Station, Integer>(st, separation); // le noeud a ajouté
		double d = dist.get(node) + g.weight(node.getObj(), st); // sa distance par rapport à son parent

		// bug : même si child est déjà dans pred, containsKey renvoie faux
		if( !pred.containsKey(child) ) {
		    System.out.println("("+child.getObj()+", "+child.getValue()+")");
		    System.out.println("\tchild.hashCode() : "+child.hashCode());
		    pred.put(child, node);
		    dist.put(child, d);
		    frontiere.add(child, d);
		}
	    }
	}

	for (Map.Entry<Pair<Station, Integer>, Pair<Station, Integer>> entry : pred.entrySet()) {
	    Pair<Station, Integer> key = entry.getKey();
	    Pair<Station, Integer> value = entry.getValue();
	    if(value != null)
		System.out.println(value.getObj().getName()+":"+value.getValue()+"-->"+key.getObj().getName()+":"+value.getValue());
	}

	for (Map.Entry<Pair<Station, Integer>, Double> entry : dist.entrySet()) {
	    Pair<Station, Integer> key = entry.getKey();
	    Double value = entry.getValue();
	    if(value != null)
		System.out.println("("+key.getObj().getName()+":"+key.getValue()+"):"+value);
	}
    }
}
