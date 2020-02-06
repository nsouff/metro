package fr.univparis.metro;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
public class ParserTest {

  @Test
  public void loadFromTest() throws IOException {
    URL url = this.getClass().getResource("/reduce-subway.txt");
    File f = new File(url.getFile());
    Parser.loadFrom(f);
    assertEquals(6, Station.stations.size());
    assertEquals("Porte de Vincennes", Station.stations.get(0).name);
  }

}
