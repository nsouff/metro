package fr.univparis.metro;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DijkstraTest{

  static WGraph<Station> w = new WGraph<Station>();

  @BeforeClass
  public static void loadFile() {
    try {
      w = Parser.loadFrom(DijkstraTest.class.getResourceAsStream("/paris.txt"));
    } catch(Exception e) {e.printStackTrace();}
  }

  @Test
  public void shortestPathTest(){
    Station a1                  = new Station("SAINT-LAZARE", "14");
    Station a2                  = new Station("CHATELET", "14");
    Station a3                  = new Station("CHATELET", "4");
    Station a4                  = new Station("SAINT-PLACIDE", "4");
    HashMap<Station, Station> prev = new HashMap<Station, Station>();
    HashMap<Station, Double> dist  = new HashMap<Station, Double>();
    Station laumS = new Station("LAUMIERE", "Meta Station Start");
    Station mdiE = new Station("MAIRIE D'ISSY", "Meta Station End");

    Dijkstra.shortestPath(w, a1, prev, dist);
    assertEquals((Double) 270.0,  dist.get(a2));
    assertEquals((Double) 330.0, dist.get(a3));
    assertEquals((Double) 0.0, dist.get(a1));
    assertEquals((Double) 840.0, dist.get(a4));
    assertEquals(new Station("MONTPARNASSE - BIENVENUE", "4"), prev.get(a4));
    assertEquals(new Station("CHATELET", "14"), prev.get(a3));
    assertEquals(new Station("PYRAMIDES", "14"), prev.get(a2));
    Dijkstra.shortestPath(w, laumS, prev, dist);
    assertEquals((Double) 2340.0, dist.get(mdiE));

    Set<Station> set = w.getVertices();
    for (Station st : set) {
      if (! st.getLine().equals("Meta Station Start"))
        continue;
      Dijkstra.shortestPath(w, st, prev, dist);
      for (Station s : set) {
        if (s.getLine().equals("Meta Station End")){
          Double d = dist.get(s);
          assertNotEquals(Double.NaN, d);
          assertNotEquals(Double.POSITIVE_INFINITY, d);
          assertNotNull(prev.get(s));
        }
      }
    }
  }
}
