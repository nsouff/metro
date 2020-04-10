package fr.univparis.metro;


import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;;

public class TraficsTest {

  @BeforeClass
  public static void initTest() throws IOException {
    Configuration.loadFrom(new File(Trafics.class.getResource("/cities.json").getFile()));
    Trafics.initTrafics();
  }

  @Test
  public void lineShutdownTest() {
    WGraph<Station> g = Trafics.getGraph("Paris");
    WGraph<Station> revert;
    revert = Trafics.lineShutdown("Paris", "1");
    Station b = new Station("Bastille", "1");
    Station gdl = new Station("Gare de Lyon", "1");
    assertEquals(Double.POSITIVE_INFINITY, g.weight(b, gdl), 0.0);
    assertEquals(Double.POSITIVE_INFINITY, g.weight(gdl, b), 0.0);

    assertEquals(25, revert.nbVertex());
    for (Station s : revert.getVertices()) {
      assertTrue(revert.neighbors(s).size() > 0);
      for (Station n : revert.neighbors(s)) {
        assertEquals(90.0, revert.weight(s, n), 0.0);
      }
    }


    g.apply(revert);
    assertEquals(90.0, g.weight(b, gdl), 0.0);
    assertEquals(90.0, g.weight(gdl, b), 0.0);

  }


  @Test
  public void lineSlowDownTest() {
    WGraph<Station> g = Trafics.getGraph("Paris");
    Station b = new Station("Bastille", "1");
    Station gdl = new Station("Gare de Lyon", "1");
    Trafics.addPerturbation("Paris", Trafics.Perturbation.LINE_SLOW_DOWN, "Line 1 slow down", new Pair<String, Double>("1", 2.0));
    assertEquals(180.0, g.weight(b, gdl), 0.0);
    assertEquals(180.0, g.weight(gdl, b), 0.0);
    assertEquals(0.0, g.weight(b, new Station("Bastille", "Meta Station End")), 0.0);
    assertEquals(60.0, g.weight(b, new Station("Bastille", "5")), 0.0);

    Trafics.revertPerturbation("Paris", "Line 1 slow down");
    assertEquals(90.0, g.weight(b, gdl), 0.0);
    assertEquals(90.0, g.weight(gdl, b), 0.0);
    assertEquals(0.0, g.weight(b, new Station("Bastille", "Meta Station End")), 0.0);
    assertEquals(60.0, g.weight(b, new Station("Bastille", "5")), 0.0);


  }


  @Test
  public void entireStationShutDownTest() {
    WGraph<Station> g = Trafics.getGraph("Paris");
    Trafics.addPerturbation("Paris", Trafics.Perturbation.ENTIRE_STATION_SHUT_DOWN, "Bastille shutdown", "Bastille");
    assertEquals(Double.POSITIVE_INFINITY, g.weight(new Station("Bastille", "5"), new Station("Bastille", "1")), 0.0);
    String[] lines = {"1", "5", "8"};
    for (String line : lines) {
      assertEquals(Double.POSITIVE_INFINITY, g.weight(new Station("Bastille", line), new Station("Bastille", "Meta Station End")), 0.0);
      assertEquals(Double.POSITIVE_INFINITY, g.weight(new Station("Bastille", "Meta Station Start"), new Station("Bastille", line)), 0.0);
    }
    // Still able to pass by the station Test:
    HashMap<Station, Double> dist = new HashMap<Station, Double>();
    HashMap<Station, Station> prev = new HashMap<Station, Station>();
    Dijkstra.shortestPath(g, new Station("Gare de Lyon", "1"), prev, dist);
    assertEquals(180.0, dist.get(new Station("Saint-Paul", "1")), 0.0);
    Dijkstra.shortestPath(g, new Station("Saint-Paul", "1"), prev, dist);
    assertEquals(180.0, dist.get(new Station("Gare de Lyon", "1")), 0.0);

    Trafics.revertPerturbation("Paris", "Bastille shutdown");
    assertEquals(60.0, g.weight(new Station("Bastille", "5"), new Station("Bastille", "1")), 0.0);
    for (String line : lines) {
      assertEquals(0.0, g.weight(new Station("Bastille", line), new Station("Bastille", "Meta Station End")), 0.0);
      assertEquals(0.0, g.weight(new Station("Bastille", "Meta Station Start"), new Station("Bastille", line)), 0.0);
    }

  }

  @Test
  public void partOfStationShutDownTest() {
    WGraph<Station> g = Trafics.getGraph("Paris");
    Station b = new Station("Bastille", "1");
    Trafics.addPerturbation("Paris", Trafics.Perturbation.PART_STATION_SHUT_DOWN, "Bastille 1 shutdown", b);
    assertEquals(Double.POSITIVE_INFINITY, g.weight(new Station("Bastille", "Meta Station Start"), b), 0.0);
    assertEquals(Double.POSITIVE_INFINITY, g.weight(b, new Station("Bastille", "Meta Station End")), 0.0);
    assertEquals(Double.POSITIVE_INFINITY, g.weight(b, new Station("Bastille", "5")), 0.0);
    assertEquals(Double.POSITIVE_INFINITY, g.weight(new Station("Bastille", "5"), b), 0.0);
    assertEquals(90.0, g.weight(new Station("Gare de Lyon", "1"), b), 0.0);
    assertEquals(90.0, g.weight(b, new Station("Gare de Lyon", "1")), 0.0);

    Trafics.revertPerturbation("Paris", "Bastille 1 shutdown");

  }

  @Test
  public void addAndRevertPerturbationTest() {
    Trafics.addPerturbation("Paris", Trafics.Perturbation.LINE_SHUTDOWN, "Line 1 shutdown", "1");
    assertEquals(Double.POSITIVE_INFINITY, Trafics.getGraph("Paris").weight(new Station("Bastille", "1"), new Station("Gare de Lyon", "1")), 0.0);
    Trafics.revertPerturbation("Paris", "Line 1 shutdown");
    assertEquals(90.0, Trafics.getGraph("Paris").weight(new Station("Bastille", "1"), new Station("Gare de Lyon", "1")), 0.0);
  }
}