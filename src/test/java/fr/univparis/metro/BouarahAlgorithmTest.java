package fr.univparis.metro;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
import java.util.function.BiPredicate;

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
	Station a1 = new Station("Courcelles", "Ligne 2");
	BouarahAlgorithm.shortestPath(g, a1, 0, sameLine);
    }

}
