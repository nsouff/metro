package fr.univparis.metro;


import java.io.File;
import java.net.URL;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;;

public class TraficsTest {
  static WGraph<Station> g;

  @BeforeClass
  public static void initTest() {
    URL url = ParserTest.class.getResource("/liste.txt");
    File f = new File(url.getFile());
    try {
      g = Parser.loadFrom(f);
    } catch(Exception e) {e.printStackTrace();}

  }

  @Test
  public void shutdownLineTest() {
    WGraph<Station> revert = Trafics.shutdownLine(g, "1");

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
}
