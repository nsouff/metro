package fr.univparis.metro;
import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
import java.io.IOException;
import java.lang.Double;

public class StatisticsTest{
    static WGraph<Station> g;

    @BeforeClass
    public static void loadFile() throws IOException{
	URL url = ParserTest.class.getResource("/liste.txt");
  	File f = new File(url.getFile());
  	g = Parser.loadFrom(f);
    }

    @Test
    public void mostDistantStationsTest(){
	Pair<Pair<Station, Station>, Double> res= Statistics.mostDistantStations(g, (s -> !s.getLine().equals("Meta Station Start")), (t -> t.getLine().equals("Meta Station End")));
	assertEquals((Double)3270. , res.getValue());
	assertEquals("Pont de Sèvres", res.getObj().getObj().toString());
	assertEquals("Créteil - Préfecture", res.getObj().getValue().toString());
	assertEquals(3, Statistics.minimumCorrespondence(g, (s -> s.getLine().equals("Meta Station Start")), s -> s.getLine().equals("Meta Station End") , (Station s1, Station s2) -> s1.getLine().equals(s2.getLine()) || s1.getLine().startsWith("Meta Station") || s2.getLine().startsWith("Meta Station")));
    }
}
