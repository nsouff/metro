package fr.univparis.metro;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
public class ParserTest {

  @Test
  public void loadFromTest() throws IOException {
    URL url = this.getClass().getResource("/liste.txt");
    File f = new File(url.getFile());
    WGraph<Station> g = Parser.loadFrom(f);

    assertEquals("Wrong size for Ligne 3bis, here are the station found: \n" + g.printVertex((t -> t.getLine().equals("Ligne 3bis"))), 4, g.nbVertex((t -> t.getLine().equals("Ligne 3bis"))));
    assertEquals("Wrong size for Ligne 7bis, here are the station found: \n" + g.printVertex((t -> t.getLine().equals("Ligne 7bis"))), 8, g.nbVertex((t -> t.getLine().equals("Ligne 7bis"))));
    int[] linesize = {25, 25, 25, 26, 22, 28, 38, 37, 37, 23, 13, 28, 32, 9};
    for (int i = 0; i < 14; i++){
      String line = "Ligne " + (i+1);
      assertEquals("Wrong size for " + line + " here are the station found :\n" + g.printVertex((t -> t.getLine().equals(line))), linesize[i], g.nbVertex((t -> t.getLine().equals(line))));
    }


  }

}
