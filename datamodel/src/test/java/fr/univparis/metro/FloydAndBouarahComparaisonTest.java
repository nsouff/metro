package fr.univparis.metro;

import java.io.*;
import java.lang.Double;
import java.lang.Integer;
import java.util.HashMap;
import java.util.function.BiPredicate;
import org.junit.*;
import static org.junit.Assert.*;

public class FloydAndBouarahComparaisonTest {

    static WGraph<Station> g;
    static BiPredicate<Station, Station> sameLine = (Station s1, Station s2) -> s1.getLine().equals(s2.getLine()) || s1.getLine().startsWith("Meta Station") || s2.getLine().startsWith("Meta Station");

    @BeforeClass
    public static void loadFile() throws IOException {
	     g = Parser.loadFrom(BouarahAlgorithmTest.class.getResourceAsStream("/paris.txt"));
    }

    @Test
    public void comparaisonTest() {

	int limit = 3;
	HashMap<Pair<Station, Integer>, Pair<Station, Integer>> prev = new HashMap<>();
	HashMap<Pair<Station, Integer>, Double> dist = new HashMap<>();
	Pair<Station, Integer> tmp;
	
	double time;
	int connections;
    
    HashMap<String, MatriceWGraph> q = MatriceWGraph.initializeAllLineGraphs(g);
	MatriceWGraph m = new MatriceWGraph(g, q);
	LimitedConnectionSearch.floyd(m.getDirect(), m.getVia(), m.getIntermediate());

	for(Station start : g.getVertices()){
		if(!start.getLine().equals("Meta Station Start")) continue;
		if(m.getSetOfVertices().get(start.getName()) == null) continue;
		prev = new HashMap<>();
		dist = new HashMap<>();
		BouarahAlgorithm.shortestPath(g, start, limit, sameLine, prev, dist);
		for(Station stop : g.getVertices()){
			if(start.getName().equals(stop.getName()) || !stop.getLine().equals("Meta Station End")) continue;
			if(m.getSetOfVertices().get(stop.getName()) == null) continue;
			tmp = new Pair<>(stop, 0);
			if(dist.get(tmp) != Double.POSITIVE_INFINITY){
				connections = m.getIntermediate()[m.getSetOfVertices().get(start.getName())][m.getSetOfVertices().get(stop.getName())] - 1;
				time = m.getDirect()[m.getSetOfVertices().get(start.getName())][m.getSetOfVertices().get(stop.getName())] + ( connections * 60.0);
				assertEquals(dist.get(tmp), time, 0.0);
				assertEquals(0, connections);
				continue;
			}
			tmp = new Pair<>(stop, 1);
			if(dist.get(tmp) != Double.POSITIVE_INFINITY){
				connections = m.getIntermediate()[m.getSetOfVertices().get(start.getName())][m.getSetOfVertices().get(stop.getName())] - 1;
				time = m.getDirect()[m.getSetOfVertices().get(start.getName())][m.getSetOfVertices().get(stop.getName())] + ( connections * 60.0);
				assertEquals(dist.get(tmp), time, 0.0);
				assertEquals(1, connections);
				continue;
			}
			tmp = new Pair<>(stop, 2);
			if(dist.get(tmp) != Double.POSITIVE_INFINITY){
				connections = m.getIntermediate()[m.getSetOfVertices().get(start.getName())][m.getSetOfVertices().get(stop.getName())] - 1;
				time = m.getDirect()[m.getSetOfVertices().get(start.getName())][m.getSetOfVertices().get(stop.getName())] + ( connections * 60.0);
				assertEquals(dist.get(tmp), time, 0.0);
				assertEquals(2, connections);
				continue;
			}
			tmp = new Pair<>(stop, 3);
			if(dist.get(tmp) != Double.POSITIVE_INFINITY){
				connections = m.getIntermediate()[m.getSetOfVertices().get(start.getName())][m.getSetOfVertices().get(stop.getName())] - 1;
				time = m.getDirect()[m.getSetOfVertices().get(start.getName())][m.getSetOfVertices().get(stop.getName())] + ( connections * 60.0);
				assertEquals(dist.get(tmp), time, 0.0);
				assertEquals(3, connections);
				continue;
			}
		}

	}

	}
    
}