package fr.univparis.metro;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
public class ParserTest {

  @Test
  public void loadFromTest() throws IOException {
    URL url1 = this.getClass().getResource("/reduce-subway.txt");
    File f1 = new File(url1.getFile());
    WGraph<Station> g1 = Parser.loadFrom(f1);
    assertEquals(5, g1.nbVertex());
    Station nation = new Station("Nation");
    assertEquals((Double) 90.0, g1.weight(nation, new Station("Avron")));
    assertEquals((Double) 90.0, g1.weight(nation, new Station("Porte de Vincennes")));
    assertEquals((Double) Double.NaN, g1.weight(new Station("Avron"), new Station("Porte de Vincennes")));
    assertEquals(3, g1.neighbors(nation).size());


    URL url2 = this.getClass().getResource("/liste.txt");
    File f2 = new File(url2.getFile());
    WGraph<Station> g2 = Parser.loadFrom(f2);
    assertEquals(302, g2.nbVertex());
  }

}
