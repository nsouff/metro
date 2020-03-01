package fr.univparis.metro;
import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class DijkstraTest{

  static WGraph<Station> w = new WGraph<Station>();

  @BeforeClass
  public static void loadFile() throws IOException {
    URL url = ParserTest.class.getResource("/liste.txt");
    File f = new File(url.getFile());
    w = Parser.loadFrom(f);
  }

  @Test
  public void shortestPathTest(){
    Station a1                  = new Station("Saint-Lazare", "14");
    Station a2                  = new Station("Châtelet", "14");
    Station a3                  = new Station("Châtelet", "4");
    Station a4                  = new Station("Saint-Placide", "4");
    HashMap<Station, Station> stat = new HashMap<Station, Station>();
    HashMap<Station, Double> dist  = new HashMap<Station, Double>();
    Dijkstra.shortestPath(w, a1, stat, dist);
    assertEquals((Double) 270.0,  dist.get(a2));
    assertEquals((Double) 330.0, dist.get(a3));
    assertEquals((Double) 0.0, dist.get(a1));
    assertEquals((Double) 840.0, dist.get(a4));
    assertEquals("Station: Montparnasse - Bienvenüe, 4", stat.get(a4).toString());
    assertEquals("Station: Châtelet, 14", stat.get(a3).toString());
    assertEquals("Station: Pyramides, 14", stat.get(a2).toString());
  }
}
