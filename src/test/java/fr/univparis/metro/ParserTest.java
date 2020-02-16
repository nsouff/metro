package fr.univparis.metro;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
public class ParserTest {

  static WGraph<Station> g;
  static Double inStationWieght = (Double) 60.0;
  static Double defaultWeight   = (Double) 90.0;
  static Double NaN             = (Double) Double.NaN;

  @BeforeClass
  public static void loadFile() throws IOException {
    URL url = ParserTest.class.getResource("/liste.txt");
    File f = new File(url.getFile());
    g = Parser.loadFrom(f);
  }


  @Test
  public void sizeTest() {
    assertEquals("Wrong size for Ligne 3bis, here are the station found: \n" + g.printVertex((t -> t.getLine().equals("Ligne 3bis"))), 4, g.nbVertex((t -> t.getLine().equals("Ligne 3bis"))));
    assertEquals("Wrong size for Ligne 7bis, here are the station found: \n" + g.printVertex((t -> t.getLine().equals("Ligne 7bis"))), 8, g.nbVertex((t -> t.getLine().equals("Ligne 7bis"))));
    int[] linesize = {25, 25, 25, 26, 22, 28, 38, 37, 37, 23, 13, 28, 32, 9};
    for (int i = 0; i < 14; i++){
      String line = "Ligne " + (i+1);
      assertEquals("Wrong size for " + line + " here are the station found :\n" + g.printVertex((t -> t.getLine().equals(line))), linesize[i], g.nbVertex((t -> t.getLine().equals(line))));
    }
  }

  @Test
  public void basicStationTest() {
    Station nation1      = new Station("Nation", "Ligne 1");
    Station nation2      = new Station("Nation", "Ligne 2");
    Station avron        = new Station("Avron", "Ligne 2");
    assertEquals(4, g.neighbors(nation2).size());
    assertEquals(inStationWieght, g.weight(new Station("Nation", "Ligne 1"), nation2));
    assertEquals(defaultWeight, g.weight(avron, nation2));
    assertEquals(defaultWeight, g.weight(nation2, avron));
    assertEquals(NaN, g.weight(nation1, avron));
    assertEquals(NaN, g.weight(avron, nation1));
  }

  @Test
  public void cycleTest() {
    Station psg      = new Station("Pré Saint-Gervais", "Ligne 7bis");
    Station pdf      = new Station("Place des Fêtes", "Ligne 7bis");
    Station danube   = new Station("Danube", "Ligne 7bis");
    Station botzaris = new Station("Botzaris", "Ligne 7bis");
    assertEquals(1, g.neighbors(psg).size());
    assertEquals(2, g.neighbors(pdf).size());
    assertEquals(1, g.neighbors(danube).size());
    assertEquals(2, g.neighbors(botzaris).size());
    assertEquals(defaultWeight, g.weight(botzaris, new Station("Buttes Chaumont", "Ligne 7bis")));
    assertEquals(defaultWeight, g.weight(botzaris, pdf));
    assertEquals(inStationWieght, g.weight(pdf, new Station("Place des Fêtes", "Ligne 11")));
    assertEquals(defaultWeight, g.weight(pdf, psg));
    assertEquals(defaultWeight, g.weight(psg, danube));
    assertEquals(defaultWeight, g.weight(danube, botzaris));
  }

  @Test
  public void forkTest() {
    Station sdu      = new Station("Saint-Denis - Université", "Ligne 13");
    Station aglc     = new Station("Asnières - Gennevilliers - Les Courtilles", "Ligne 13");
    Station gm       = new Station("Guy Môquet", "Ligne 13");
    Station brochant = new Station("Brochant", "Ligne 13");
    Station lf       = new Station("La Fourche", "Ligne 13");
    assertEquals(1, g.neighbors(sdu).size());
    assertEquals(1, g.neighbors(aglc).size());
    assertEquals(2, g.neighbors(gm).size());
    assertEquals(2, g.neighbors(brochant).size());
    assertEquals(3, g.neighbors(lf).size());
    assertEquals(defaultWeight, g.weight(sdu, new Station("Basilique de Saint-Denis", "Ligne 13")));
    assertEquals(defaultWeight, g.weight(aglc, new Station("Les Agnettes", "Ligne 13")));
    assertEquals(defaultWeight, g.weight(gm, lf));
    assertEquals(defaultWeight, g.weight(gm, new Station("Porte de Saint-Ouen", "Ligne 13")));
    assertEquals(defaultWeight, g.weight(brochant, lf));
    assertEquals(defaultWeight, g.weight(brochant, new Station("Porte de Clichy", "Ligne 13")));
    assertEquals(defaultWeight, g.weight(lf, brochant));
    assertEquals(defaultWeight, g.weight(lf, gm));
    assertEquals(defaultWeight, g.weight(lf, new Station("Place de Clichy", "Ligne 13")));
  }

}
