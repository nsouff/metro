package fr.univparis.metro;

import java.io.*;
import java.lang.Double;
import java.lang.Integer;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import org.junit.*;
import static org.junit.Assert.*;

public class BouarahAlgorithmTest {
    static WGraph<Station> g;
    static BiPredicate<Station, Station> sameLine = (Station s1, Station s2) -> s1.getLine().equals(s2.getLine());
    
    @BeforeClass
    public static void loadFile() throws IOException {
	URL url = ParserTest.class.getResource("/liste.txt");
	File f = new File(url.getFile());
	g = Parser.loadFrom(f);
    }

    @Test
    public void shortestPathTest() {
	Station start = new Station("Courcelles", "2");
	Station stop1 = new Station("La Défense - Grande Arche", "1");
	
	int limit = 1;
	HashMap<Pair<Station, Integer>, Pair<Station, Integer>> prev = new HashMap<>();
	HashMap<Pair<Station, Integer>, Double> dist = new HashMap<>();
	
	BouarahAlgorithm.shortestPath(g, start, limit, sameLine, prev, dist);

	Pair<Station, Integer> tmp = new Pair<>(stop1, 1);
	assertEquals(780.0, dist.get(tmp), 0.0);

	tmp = new Pair<>(stop1, 0);
	assertEquals(Double.POSITIVE_INFINITY, dist.get(tmp), 0.0);
    }

}
