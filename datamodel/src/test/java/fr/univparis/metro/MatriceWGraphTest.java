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
        
        ArrayList<Pair<String, String>> l = LimitedConnectionSearch.getPath(m, "Porte des Lilas", "Temple");
        assertEquals(m.getDirect()[124][216], 720.0, 0.0);

        assertEquals(l.get(0).getObj(), "Temple");
        assertEquals(l.get(0).getValue(), "FIN");

        assertEquals(l.get(1).getObj(), "Gambetta");
        assertEquals(l.get(1).getValue(), "Ligne 3");
            
        assertEquals(l.get(2).getObj(), "Porte des Lilas");
        assertEquals(l.get(2).getValue(), "Ligne 3bis");

        ArrayList<Pair<String, String>> t = LimitedConnectionSearch.getPath(m, "Charles de Gaulle - Etoile", "République");

        assertEquals(t.get(0).getObj(), "République");
        assertEquals(t.get(0).getValue(), "FIN");

        assertEquals(t.get(1).getObj(), "Châtelet");
        assertEquals(t.get(1).getValue(), "Ligne 11");
            
        assertEquals(t.get(2).getObj(), "Charles de Gaulle - Etoile");
        assertEquals(t.get(2).getValue(), "Ligne 1");

        ArrayList<Pair<String, String>> a = LimitedConnectionSearch.getPath(m, "Robespierre", "Marx Dormoy");

        assertEquals(a.get(0).getObj(), "Marx Dormoy");
        assertEquals(a.get(0).getValue(), "FIN");

        assertEquals(a.get(1).getObj(), "Marcadet - Poissonniers");
        assertEquals(a.get(1).getValue(), "Ligne 12");
            
        assertEquals(a.get(2).getObj(), "Strasbourg - Saint-Denis");
        assertEquals(a.get(2).getValue(), "Ligne 4");

        assertEquals(a.get(3).getObj(), "Robespierre");
        assertEquals(a.get(3).getValue(), "Ligne 9");

        ArrayList<Pair<String, String>> k = LimitedConnectionSearch.getPath(m, "Botzaris", "Chemin Vert");

        assertEquals(k.get(0).getObj(), "Chemin Vert");
        assertEquals(k.get(0).getValue(), "FIN");

        assertEquals(k.get(1).getObj(), "République");
        assertEquals(k.get(1).getValue(), "Ligne 8");
            
        assertEquals(k.get(2).getObj(), "Place des Fêtes");
        assertEquals(k.get(2).getValue(), "Ligne 11");

        assertEquals(k.get(3).getObj(), "Botzaris");
        assertEquals(k.get(3).getValue(), "Ligne 7bis");

        ArrayList<Pair<String, String>> u = LimitedConnectionSearch.getPath(m, "Grands Boulevards", "Michel-Ange - Molitor");

        assertEquals(u.get(0).getObj(), "Michel-Ange - Molitor");
        assertEquals(u.get(0).getValue(), "FIN");

        assertEquals(u.get(1).getObj(), "Grands Boulevards");
        assertEquals(u.get(1).getValue(), "Ligne 9");

        ArrayList<Pair<String, String>> n = LimitedConnectionSearch.getPath(m, "Gabriel Péri", "Mirabeau");

        assertEquals(n.get(0).getObj(), "Mirabeau");
        assertEquals(n.get(0).getValue(), "FIN");

        assertEquals(n.get(1).getObj(), "Duroc");
        assertEquals(n.get(1).getValue(), "Ligne 10");
            
        assertEquals(n.get(2).getObj(), "Gabriel Péri");
        assertEquals(n.get(2).getValue(), "Ligne 13");

        ArrayList<Pair<String, String>> i = LimitedConnectionSearch.getPath(m, "Porte Dauphine", "Olympiades");

        assertEquals(i.get(0).getObj(), "Olympiades");
        assertEquals(i.get(0).getValue(), "FIN");

        assertEquals(i.get(1).getObj(), "Châtelet");
        assertEquals(i.get(1).getValue(), "Ligne 14");
            
        assertEquals(i.get(2).getObj(), "Charles de Gaulle - Etoile");
        assertEquals(i.get(2).getValue(), "Ligne 1");

        assertEquals(i.get(3).getObj(), "Porte Dauphine");
        assertEquals(i.get(3).getValue(), "Ligne 2");
       
        //gfor(Pair<String, String> p : i) System.out.println(p.getValue() + " -> " + p.getObj());
    }
}