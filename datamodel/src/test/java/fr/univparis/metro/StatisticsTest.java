package fr.univparis.metro;
import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
import java.io.IOException;
import java.lang.Double;
import java.util.HashMap;

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
      assertEquals("Pont de Sèvres", res.getObj().getObj().getName());
      assertEquals("Créteil - Préfecture", res.getObj().getValue().getName());
    }

    @Test
    public void minimumCorrespondenceTest(){
      assertEquals(3, Statistics.minimumCorrespondence(g, (s -> s.getLine().equals("Meta Station Start")), s -> s.getLine().equals("Meta Station End") , (Station s1, Station s2) -> s1.getLine().equals(s2.getLine()) || s1.getLine().startsWith("Meta Station") || s2.getLine().startsWith("Meta Station")));
    }

    @Test
    public void extremumLineTest() {
      assertEquals("3bis", Statistics.extremumLine(g, false));
      assertEquals("7", Statistics.extremumLine(g, true));
    }

    @Test
    public void averageTimeOnEachLineTest(){
      HashMap<String, Double> res = new HashMap<String, Double>();
      res = Statistics.averageTimeOnEachLine(g);
      assertEquals((Double)2160. , (Double)res.get("1"));
      assertEquals((Double)2160. , (Double)res.get("2"));
      assertEquals((Double)2160. , (Double)res.get("3"));
      assertEquals((Double)270. , (Double)res.get("3bis"));
      assertEquals((Double)2250. , (Double)res.get("4"));
      assertEquals((Double)1890. , (Double)res.get("5"));
      assertEquals((Double)2430. , (Double)res.get("6"));
      assertEquals((Double)2970. , (Double)res.get("7"));
      assertEquals((Double)630. , (Double)res.get("7bis"));
      assertEquals((Double)3240. , (Double)res.get("8"));
      assertEquals((Double)3240. , (Double)res.get("9"));
      assertEquals((Double)1890. , (Double)res.get("10"));
      assertEquals((Double)1080. , (Double)res.get("11"));
      assertEquals((Double)2430. , (Double)res.get("12"));
      assertEquals((Double)2250. , (Double)res.get("13"));
      assertEquals((Double)720. , (Double)res.get("14"));
    }
}
