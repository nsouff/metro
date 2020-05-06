package fr.univparis.metro;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.io.IOException;
import java.lang.Double;
import java.util.HashMap;

public class StatisticsTest{
    static WGraph<Station> g;

    @BeforeClass
    public static void loadFile() throws IOException{
      InputStream i = ParserTest.class.getResourceAsStream("/liste.txt");
      g = Parser.loadFrom(i);
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
      int n = Statistics.averageTimeOnEachLine(g, res);
      assertEquals((Double)2160. , (Double)res.get("1"));
      assertEquals((Double)2160. , (Double)res.get("2"));
      assertEquals((Double)2160. , (Double)res.get("3"));
      assertEquals((Double)270. , (Double)res.get("3bis"));
      assertEquals((Double)720. , (Double)res.get("14"));
      assertEquals(1993 , n);
    }

    @Test
    public void shortestTimeTravelLineTest(){
      assertEquals("3bis", Statistics.shortestTimeTravelLine(g).getObj());
      assertEquals((Double)270., Statistics.shortestTimeTravelLine(g).getValue());
    }

    @Test
    public void longestTimeTravelLineTest(){
      assertEquals("8", Statistics.longestTimeTravelLine(g).getObj());
      assertEquals((Double)3240., Statistics.longestTimeTravelLine(g).getValue());
    }

    public void averageNbOfStationPerLine(){
      assertEquals(23, Statistics.averageNbOfStationPerLine(g));

    }
}
