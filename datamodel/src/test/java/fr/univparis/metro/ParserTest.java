package fr.univparis.metro;

import java.util.HashMap;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
public class ParserTest {

  static WGraph<Station> g;
  static Double inStationWieght = (Double) 60.0;
  static Double defaultWeight   = (Double) 90.0;
  static Double NaN             = (Double) Double.NaN;

  @BeforeClass
  public static void loadFile() throws IOException {
    g = Parser.loadFrom(ParserTest.class.getResourceAsStream("/liste.txt"));
  }


  @Test
  public void sizeTest() {
    assertEquals("Wrong size for line 3bis, here are the station found: \n" + g.vertexToString((t -> t.getLine().equals("3bis"))), 4, g.nbVertex((t -> t.getLine().equals("3bis"))));
    assertEquals("Wrong size for line 7bis, here are the station found: \n" + g.vertexToString((t -> t.getLine().equals("7bis"))), 8, g.nbVertex((t -> t.getLine().equals("7bis") && !t.getName().contains("$2"))));
    int[] linesize = {25, 25, 25, 26, 22, 28, 38, 37, 37, 23, 13, 28, 32, 9};
    for (int i = 0; i < 14; i++){
      String line = "" + (i+1);
      assertEquals("Wrong size for " + line + " here are the station found :\n" + g.vertexToString((t -> t.getLine().equals(line))), linesize[i], g.nbVertex((t -> t.getLine().equals(line) && ! t.getName().contains("$2"))));
    }
  }

  @Test
  public void basicStationTest() {
    Station nation1      = new Station("Nation", "1");
    Station nation2      = new Station("Nation", "2");
    Station avron        = new Station("Avron", "2");
    assertEquals(5, g.neighbors(nation2).size());
    assertEquals(inStationWieght, g.weight(new Station("Nation", "1"), nation2));
    assertEquals(defaultWeight, g.weight(avron, nation2));
    assertEquals(defaultWeight, g.weight(nation2, avron));
    assertEquals(NaN, g.weight(nation1, avron));
    assertEquals(NaN, g.weight(avron, nation1));
  }

  @Test
  public void cycleTest() {
    Station psg1      = new Station("Pré Saint-Gervais$1", "7bis");
    Station psg2      = new Station("Pré Saint-Gervais$2", "7bis");
    Station pdf       = new Station("Place des Fêtes", "7bis");
    Station danube    = new Station("Danube", "7bis");
    Station botzaris1 = new Station("Botzaris$1", "7bis");
    Station botzaris2 = new Station("Botzaris$2", "7bis");
    assertEquals(2, g.neighbors(psg1).size());
    assertEquals(3, g.neighbors(psg2).size());
    assertEquals(3, g.neighbors(pdf).size());
    assertEquals(2, g.neighbors(danube).size());
    assertEquals(3, g.neighbors(botzaris1).size());
    assertEquals(4, g.neighbors(botzaris2).size());

    assertEquals(inStationWieght, g.weight(botzaris1, botzaris2));
    assertEquals(inStationWieght, g.weight(botzaris2, botzaris1));
    assertEquals(inStationWieght, g.weight(psg1, psg2));
    assertEquals(inStationWieght, g.weight(psg2, psg1));
    assertEquals(defaultWeight, g.weight(botzaris1, new Station("Buttes Chaumont", "7bis")));
    assertEquals(defaultWeight, g.weight(botzaris2, new Station("Buttes Chaumont", "7bis")));
    assertEquals(Double.NaN, g.weight(botzaris1, pdf), 0.0);
    assertEquals(defaultWeight, g.weight(botzaris2, pdf));
    assertEquals(inStationWieght, g.weight(pdf, new Station("Place des Fêtes", "11")));
    assertEquals(defaultWeight, g.weight(pdf, psg1));
    assertEquals(defaultWeight, g.weight(psg2, danube));
    assertEquals(Double.NaN, g.weight(psg1, danube), 0.0);
    assertEquals(defaultWeight, g.weight(danube, botzaris1));
  }

  @Test
  public void forkTest() {
    Station sdu      = new Station("Saint-Denis - Université", "13");
    Station aglc     = new Station("Asnières - Gennevilliers - Les Courtilles", "13");
    Station gm       = new Station("Guy Môquet", "13");
    Station brochant = new Station("Brochant", "13");
    Station lf1       = new Station("La Fourche$1", "13");
    Station lf2       = new Station("La Fourche$2", "13");
    assertEquals(2, g.neighbors(sdu).size());
    assertEquals(2, g.neighbors(aglc).size());
    assertEquals(3, g.neighbors(gm).size());
    assertEquals(3, g.neighbors(brochant).size());
    assertEquals(4, g.neighbors(lf1).size());
    assertEquals(4, g.neighbors(lf2).size());
    assertEquals(defaultWeight, g.weight(sdu, new Station("Basilique de Saint-Denis", "13")));
    assertEquals(defaultWeight, g.weight(aglc, new Station("Les Agnettes", "13")));
    assertEquals(defaultWeight, g.weight(gm, lf1));
    assertEquals(defaultWeight, g.weight(gm, new Station("Porte de Saint-Ouen", "13")));
    assertEquals(Double.NaN, g.weight(brochant, lf1), 0.0);
    assertEquals(defaultWeight, g.weight(brochant, lf2), 0.0);
    assertEquals(defaultWeight, g.weight(brochant, new Station("Porte de Clichy", "13")));
    assertEquals(Double.NaN, g.weight(lf1, brochant), 0.0);
    assertEquals(defaultWeight, g.weight(lf2, brochant));
    assertEquals(defaultWeight, g.weight(lf1, gm));
    assertEquals(defaultWeight, g.weight(lf1, new Station("Place de Clichy", "13")));

    HashMap<Station, Double> dist = new HashMap<Station, Double>();
    HashMap<Station, Station> prev = new HashMap<Station, Station>();
    Station ptiS = new Station("Porte d'Italie", "Meta Station Start");
    Station ptiE = new Station("Porte d'Italie", "Meta Station End");
    Station kbS = new Station("Le Kremlin-Bicêtre", "Meta Station Start");
    Station kbE = new Station("Le Kremlin-Bicêtre", "Meta Station End");

    Dijkstra.shortestPath(g, ptiS, prev, dist);
    assertEquals(240.0, dist.get(kbE), 0.0);
    Dijkstra.shortestPath(g, kbS, prev, dist);
    assertEquals(240.0, dist.get(ptiE), 0.0);

  }

  @Test
  public void metaStatTest() {
    Station cStart = new Station("Châtelet", "Meta Station Start");
    Station cEnd   = new Station("Châtelet", "Meta Station End");
    assertEquals(5, g.neighbors(cStart).size());
    assertEquals(0, g.neighbors(cEnd).size());
    assertEquals((Double) 0.0, g.weight(new Station("Châtelet", "4"), cEnd));
    assertEquals((Double) 0.0, g.weight(cStart, new Station("Châtelet", "4")));

  }

}
