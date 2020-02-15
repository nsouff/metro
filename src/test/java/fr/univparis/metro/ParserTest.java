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
    


    URL url2 = this.getClass().getResource("/liste.txt");
    File f2 = new File(url2.getFile());
    WGraph<Station> g2 = Parser.loadFrom(f2);


  }

}
