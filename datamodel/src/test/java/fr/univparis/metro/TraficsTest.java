package fr.univparis.metro;


import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;;

public class TraficsTest {

  @BeforeClass
  public static void initTest() {
    Configuration.loadFrom(Trafics.class.getResourceAsStream("/cities.json"));
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

  @Test
  public void partOfLineShutDownTest() {
    WGraph<Station> g = Trafics.getGraph("Paris");
    Station j = new Station("Jaurès", "5");
    Station l = new Station("Laumière", "5");
    Station o = new Station("Ourcq", "5");
    Station s = new Station("Stalingrad", "5");
    Station p = new Station("Porte de Pantin", "5");
    Trafics.addPerturbation("Paris", Trafics.Perturbation.PART_LINE_SHUT_DOWN, "Jaurès to Ourcq shutdown", new Pair<Station, Station>(j, o));
    assertEquals(Double.POSITIVE_INFINITY, g.weight(j, l), 0.0);
    assertEquals(Double.POSITIVE_INFINITY, g.weight(l, j), 0.0);
    assertEquals(Double.POSITIVE_INFINITY, g.weight(o, l), 0.0);
    assertEquals(Double.POSITIVE_INFINITY, g.weight(l, o), 0.0);
    assertEquals(90.0, g.weight(p, o), 0.0);
    assertEquals(90.0, g.weight(o, p), 0.0);
    assertEquals(90.0, g.weight(j, s), 0.0);
    assertEquals(90.0, g.weight(j, s), 0.0);

    Trafics.revertPerturbation("Paris", "Jaurès to Ourcq shutdown");
  }

  @Test
  public void partOfLineSlowDown() {
    WGraph<Station> g = Trafics.getGraph("Paris");
    Station j = new Station("Jaurès", "5");
    Station l = new Station("Laumière", "5");
    Station o = new Station("Ourcq", "5");
    Station s = new Station("Stalingrad", "5");
    Station p = new Station("Porte de Pantin", "5");
    Object[] objs = {j, o, 2.0};
    Trafics.addPerturbation("Paris", Trafics.Perturbation.PART_LINE_SLOW_DOWN, "Jaurès to Ourcq slow down", objs);
    assertEquals(180.0, g.weight(j, l), 0.0);
    assertEquals(180.0, g.weight(l, j), 0.0);
    assertEquals(180.0, g.weight(o, l), 0.0);
    assertEquals(180.0, g.weight(l, o), 0.0);
    assertEquals(90.0, g.weight(p, o), 0.0);
    assertEquals(90.0, g.weight(o, p), 0.0);
    assertEquals(90.0, g.weight(j, s), 0.0);
    assertEquals(90.0, g.weight(j, s), 0.0);

    Trafics.revertPerturbation("Paris", "Jaurès to Ourcq slow down");
  }

  @Test
  public void allTraficsSlowDownTest() {
    WGraph<Station> g = Trafics.getGraph("Paris");
    Station m8 = new Station("Madeleine", "8");
    Station c  = new Station("Concorde", "Meta Station End");
    Station c1 = new Station("Concorde", "1");
    Station c8 = new Station("Concorde", "8");

    Trafics.addPerturbation("Paris", Trafics.Perturbation.ALL_TRAFICS_SLOW_DOWN, "Snow on the rails", 2.0);
    assertEquals(180.0, g.weight(m8, c8), 0.0);
    assertEquals(180.0, g.weight(c8, m8), 0.0);

    assertEquals(60.0, g.weight(c8, c1), 0.0);
    assertEquals(60.0, g.weight(c1, c8), 0.0);

    assertEquals(0.0, g.weight(c8, c), 0.0);
    assertEquals(0.0, g.weight(c1, c), 0.0);

    Trafics.revertPerturbation("Paris", "Snow on the rails");
    assertEquals(90.0, g.weight(m8, c8), 0.0);
    assertEquals(90.0, g.weight(c8, m8), 0.0);

    assertEquals(60.0, g.weight(c8, c1), 0.0);
    assertEquals(60.0, g.weight(c1, c8), 0.0);

    assertEquals(0.0, g.weight(c8, c), 0.0);
    assertEquals(0.0, g.weight(c1, c), 0.0);
  }

}
