package fr.univparis.metro;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class MatriceWGraphTest{

    static WGraph<Station> g;

    @BeforeClass
    public static void loadFile() throws IOException {
        URL url = ParserTest.class.getResource("/liste.txt");
        File f = new File(url.getFile());
        g = Parser.loadFrom(f);
    }

    @Test
    public void matriceWGraphTest(){
        MatriceWGraph m = new MatriceWGraph(g);
        /*
        ArrayList<Pair<String, String>> l = LimitedConnectionSearch.getPath(m, "Porte des Lilas", "Temple");
        System.out.println(m.getSetOfVertices().get("Gare d'Austerlitz"));
        System.out.println(m.getSetOfVertices().get("Jussieu"));
        assertEquals(m.getDirect()[124][216], 720.0, 0.0);

        assertEquals(l.get(0).getObj(), "Temple");
        assertEquals(l.get(0).getValue(), "FIN");

        assertEquals(l.get(1).getObj(), "Gambetta");
        assertEquals(l.get(1).getValue(), "Ligne 3");
            
        assertEquals(l.get(2).getObj(), "Porte des Lilas");
        assertEquals(l.get(2).getValue(), "Ligne 3bis");
        */

        ArrayList<Pair<String, String>> l = LimitedConnectionSearch.getPath(m, "Ourcq", "République");
        System.out.println(m.getVia()[m.getSetOfVertices().get("Stalingrad")][m.getSetOfVertices().get("République")].getName());
        assertEquals(m.getDirect()[m.getSetOfVertices().get("République")][m.getSetOfVertices().get("Stalingrad")], 360.0, 0.0);
        for(Pair<String, String> p : l) System.out.println(p.getValue() + " -> " + p.getObj());
    }
}