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
	     g = Parser.loadFrom(BouarahAlgorithmTest.class.getResourceAsStream("/liste.txt"));
    }

    @Test
    public void shortestPathTest() {
	Station start = new Station("Courcelles", "Meta Station Start");
	Station stop1 = new Station("La Défense - Grande Arche", "Meta Station End");
	Station stop2 = new Station("Nation", "Meta Station End");
	Station stop3 = new Station("Créteil - Préfecture", "Meta Station End");

	int limit = 2;
	HashMap<Pair<Station, Integer>, Pair<Station, Integer>> prev = new HashMap<>();
	HashMap<Pair<Station, Integer>, Double> dist = new HashMap<>();
	
	double time;
	int connections;
    
    HashMap<String, MatriceWGraph> q = MatriceWGraph.initializeAllLineGraphs(g);
	MatriceWGraph m = new MatriceWGraph(g, q);
	LimitedConnectionSearch.floyd(m.getDirect(), m.getVia(), m.getIntermediate());

	BouarahAlgorithm.shortestPath(g, start, limit, sameLine, prev, dist);

	Pair<Station, Integer> tmp = new Pair<>(stop1, 1);
	assertEquals(780.0, dist.get(tmp), 0.0); // Courcelles(2) -> ... -> Charles de Gaulle Etoile(1,2,6) -> ... -> La Défense(1)
	assertNotNull(prev.get(tmp));

	tmp = new Pair<>(stop1, 0);
	assertEquals(Double.POSITIVE_INFINITY, dist.get(tmp), 0.0);
    assertNull(prev.get(tmp));
	
	connections = m.getIntermediate()[m.getSetOfVertices().get(start.getName())][m.getSetOfVertices().get(stop1.getName())] - 1;
	time = m.getDirect()[m.getSetOfVertices().get(start.getName())][m.getSetOfVertices().get(stop1.getName())] + ( connections * 60.0 );
    assertEquals(1, connections);
    assertEquals(780.0, time, 0.0);

	tmp = new Pair<>(stop2, 0);
	assertEquals(1800.0, dist.get(tmp), 0.0); // Reste toujours sur la ligne 2
	assertNotNull(prev.get(tmp));

	tmp = new Pair<>(stop2, 1);
	assertEquals(16*90.0 + 60.0, dist.get(tmp), 0.0); // Courcelles(2) -> ... -> Charles de Gaulle Etoile(1,2,6) -> ... -> Nation(1)
    assertNotNull(prev.get(tmp));
	
	connections = m.getIntermediate()[m.getSetOfVertices().get(start.getName())][m.getSetOfVertices().get(stop2.getName())] - 1;
	time = m.getDirect()[m.getSetOfVertices().get(start.getName())][m.getSetOfVertices().get(stop2.getName())] + ( connections * 60.0 );
    assertEquals(0, connections);
    assertEquals(1800.0, time, 0.0);

	tmp = new Pair<>(stop3, 1);
	assertEquals(Double.POSITIVE_INFINITY, dist.get(tmp), 0.0);
	assertNull(prev.get(tmp));

	tmp = new Pair<>(stop3, 2);
	assertEquals(2640.0, dist.get(tmp), 0.0);  // Courcelles(2) -> ... -> Charles de Gaulle Etoile(1,2,6) -> ... -> Reuilly-Diderot(1,8) -> ...  -> Créteil - Préfecture (8)
	assertNotNull(prev.get(tmp));

	tmp = new Pair<>(stop3, 3);
	assertNull(dist.get(tmp));
    assertNull(prev.get(tmp));
    
    connections = m.getIntermediate()[m.getSetOfVertices().get(start.getName())][m.getSetOfVertices().get(stop3.getName())] - 1;
	time = m.getDirect()[m.getSetOfVertices().get(start.getName())][m.getSetOfVertices().get(stop3.getName())] + ( connections * 60.0 );
    assertEquals(0, connections);
    assertEquals(2640.0, time, 0.0);
    }
    
}