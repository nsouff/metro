package fr.univparis.metro;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class MatriceWGraphTest{

    static WGraph<Station> g;

    @BeforeClass
    public static void loadFile() throws IOException {
        g = Parser.loadFrom(MatriceWGraph.class.getResourceAsStream("/paris.txt"));
    }

    @Test
    public void matriceWGraphTest(){
        HashMap<String, MatriceWGraph> q = MatriceWGraph.initializeAllLineGraphs(g);
        MatriceWGraph m = new MatriceWGraph(g, q);
        LimitedConnectionSearch.floyd(m.getDirect(), m.getVia(), m.getIntermediate());

        ArrayList<Pair<String, String>> l = LimitedConnectionSearch.getPath(m, "PORTE DES LILAS", "TEMPLE");
        //for(Pair<String, String> p : l) System.out.println(p.getValue() + " -> " + p.getObj());
        assertEquals(m.getDirect()[m.getSetOfVertices().get("PORTE DES LILAS")][m.getSetOfVertices().get("TEMPLE")], 720.0, 0.0);
        assertEquals(l.get(0).getObj(), "TEMPLE");
        assertEquals(l.get(0).getValue(), "FIN");
        assertEquals(l.get(1).getObj(), "REPUBLIQUE");
        assertEquals(l.get(1).getValue(), "3");
        assertEquals(l.get(2).getObj(), "PORTE DES LILAS");
        assertEquals(l.get(2).getValue(), "11");

        ArrayList<Pair<String, String>> t = LimitedConnectionSearch.getPath(m, "CHARLES DE GAULLE - ETOILE", "REPUBLIQUE");
        //for(Pair<String, String> p : t) System.out.println(p.getValue() + " -> " + p.getObj());
        assertEquals(m.getDirect()[m.getSetOfVertices().get("CHARLES DE GAULLE - ETOILE")][m.getSetOfVertices().get("REPUBLIQUE")], 990.0, 0.0);
        assertEquals(t.get(0).getObj(), "REPUBLIQUE");
        assertEquals(t.get(0).getValue(), "FIN");
        assertEquals(t.get(1).getObj(), "CONCORDE");
        assertEquals(t.get(1).getValue(), "8");
        assertEquals(t.get(2).getObj(), "CHARLES DE GAULLE - ETOILE");
        assertEquals(t.get(2).getValue(), "1");

        ArrayList<Pair<String, String>> a = LimitedConnectionSearch.getPath(m, "ROBESPIERRE", "MARX DORMOY");
        //for(Pair<String, String> p : a) System.out.println(p.getValue() + " -> " + p.getObj());
        assertEquals(m.getDirect()[m.getSetOfVertices().get("ROBESPIERRE")][m.getSetOfVertices().get("MARX DORMOY")], 1620.0, 0.0);
        assertEquals(a.get(0).getObj(), "MARX DORMOY");
        assertEquals(a.get(0).getValue(), "FIN");
        assertEquals(a.get(1).getObj(), "MARCADET - POISSONNIERS");
        assertEquals(a.get(1).getValue(), "12");
        assertEquals(a.get(2).getObj(), "STRASBOURG - SAINT-DENIS");
        assertEquals(a.get(2).getValue(), "4");
        assertEquals(a.get(3).getObj(), "ROBESPIERRE");
        assertEquals(a.get(3).getValue(), "9");

        ArrayList<Pair<String, String>> k = LimitedConnectionSearch.getPath(m, "BOTZARIS$2", "CHEMIN VERT");
        //for(Pair<String, String> p : k) System.out.println(p.getValue() + " -> " + p.getObj());
        assertEquals(m.getDirect()[m.getSetOfVertices().get("BOTZARIS$2")][m.getSetOfVertices().get("CHEMIN VERT")], 810.0, 0.0);
        assertEquals(k.get(0).getObj(), "CHEMIN VERT");
        assertEquals(k.get(0).getValue(), "FIN");
        assertEquals(k.get(1).getObj(), "REPUBLIQUE");
        assertEquals(k.get(1).getValue(), "8");
        assertEquals(k.get(2).getObj(), "PLACE DES FETES");
        assertEquals(k.get(2).getValue(), "11");
        assertEquals(k.get(3).getObj(), "BOTZARIS$2");
        assertEquals(k.get(3).getValue(), "7BIS");

        ArrayList<Pair<String, String>> u = LimitedConnectionSearch.getPath(m, "GRANDS BOULEVARDS", "MICHEL-ANGE - MOLITOR");
        //for(Pair<String, String> p : u) System.out.println(p.getValue() + " -> " + p.getObj());
        assertEquals(m.getDirect()[m.getSetOfVertices().get("GRANDS BOULEVARDS")][m.getSetOfVertices().get("MICHEL-ANGE - MOLITOR")], 1440.0, 0.0);
        assertEquals(u.get(0).getObj(), "MICHEL-ANGE - MOLITOR");
        assertEquals(u.get(0).getValue(), "FIN");
        assertEquals(u.get(1).getObj(), "GRANDS BOULEVARDS");
        assertEquals(u.get(1).getValue(), "9");

        ArrayList<Pair<String, String>> n = LimitedConnectionSearch.getPath(m, "GABRIEL PERI", "MIRABEAU");
        //for(Pair<String, String> p : n) System.out.println(p.getValue() + " -> " + p.getObj());
        assertEquals(m.getDirect()[m.getSetOfVertices().get("GABRIEL PERI")][m.getSetOfVertices().get("MIRABEAU")], 2310.0, 0.0);
        assertEquals(n.get(0).getObj(), "MIRABEAU");
        assertEquals(n.get(0).getValue(), "FIN");
        assertEquals(n.get(1).getObj(), "DUROC");
        assertEquals(n.get(1).getValue(), "10");
        assertEquals(n.get(2).getObj(), "GABRIEL PERI");
        assertEquals(n.get(2).getValue(), "13");

        ArrayList<Pair<String, String>> i = LimitedConnectionSearch.getPath(m, "PORTE DAUPHINE", "OLYMPIADES");
        //for(Pair<String, String> p : i) System.out.println(p.getValue() + " -> " + p.getObj());
        assertEquals(m.getDirect()[m.getSetOfVertices().get("PORTE DAUPHINE")][m.getSetOfVertices().get("OLYMPIADES")], 1350.0, 0.0);
        assertEquals(i.get(0).getObj(), "OLYMPIADES");
        assertEquals(i.get(0).getValue(), "FIN");
        assertEquals(i.get(1).getObj(), "CHATELET");
        assertEquals(i.get(1).getValue(), "14");
        assertEquals(i.get(2).getObj(), "CHARLES DE GAULLE - ETOILE");
        assertEquals(i.get(2).getValue(), "1");
        assertEquals(i.get(3).getObj(), "PORTE DAUPHINE");
        assertEquals(i.get(3).getValue(), "2");
    }
}
