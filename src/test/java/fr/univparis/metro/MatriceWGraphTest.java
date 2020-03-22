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
        LinkedList<String> l = LimitedConnectionSearch.getPath(m, "Malakoff - Plateau de Vanves", "Anatole France");
        for(String s : l) System.out.println(s);

    }
}