package fr.univparis.metro;
import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
import java.io.IOException;
import java.util.*;
import java.lang.Double;

public class StatisticsTest{
  static WGraph<Station> g;

  @BeforeClass
  public static void loadFile() throws IOException{
    URL url = ParserTest.class.getResource("/liste.txt");
  	File f = new File(url.getFile());
  	g = Parser.loadFrom(f);
  }

  @Test
  public void mostDistantStationsTest(){
    Pair<Pair<Station, Station>, Double> res= Statistics.mostDistantStations(g, (s -> !s.getLine().equals("Meta Station Start")), (t -> t.getLine().equals("Meta Station End")));
    assertEquals((Double)3270. , res.getValue());
    assertEquals("Pont de Sèvres", res.getObj().getObj().toString());
    assertEquals("Créteil - Préfecture", res.getObj().getValue().toString());
  }
}

