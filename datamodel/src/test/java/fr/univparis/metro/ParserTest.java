package fr.univparis.metro;

import java.util.HashMap;
import org.junit.*;
import static org.junit.Assert.*;
public class ParserTest {

  static WGraph<Station> g;
  static Double inStationWieght = (Double) 60.0;
  static Double defaultWeight   = (Double) 90.0;
  static Double NaN             = (Double) Double.NaN;

  @BeforeClass
  public static void loadFile(){
    g = Parser.loadFrom(ParserTest.class.getResourceAsStream("/liste.txt"));
  }


  @Test
  public void sizeTest() {
    assertEquals("Wrong size for line 3BIS, here are the station found: \n" + g.vertexToString((t -> t.getLine().equals("3BIS"))), 4, g.nbVertex((t -> t.getLine().equals("3BIS"))));
    assertEquals("Wrong size for line 7BIS, here are the station found: \n" + g.vertexToString((t -> t.getLine().equals("7BIS"))), 8, g.nbVertex((t -> t.getLine().equals("7BIS") && !t.getName().contains("$2"))));
    int[] linesize = {25, 25, 25, 26, 22, 28, 38, 37, 37, 23, 13, 28, 32, 9};
    for (int i = 0; i < 14; i++){
      String line = "" + (i+1);
      assertEquals("Wrong size for " + line + " here are the station found :\n" + g.vertexToString((t -> t.getLine().equals(line))), linesize[i], g.nbVertex((t -> t.getLine().equals(line) && ! t.getName().contains("$2"))));
    }
  }

  @Test
  public void basicStationTest() {
    Station nation1      = new Station("NATION", "1");
    Station nation2      = new Station("NATION", "2");
    Station avron        = new Station("AVRON", "2");
    assertEquals(5, g.neighbors(nation2).size());
    assertEquals(inStationWieght, g.weight(new Station("NATION", "1"), nation2));
    assertEquals(defaultWeight, g.weight(avron, nation2));
    assertEquals(defaultWeight, g.weight(nation2, avron));
    assertEquals(NaN, g.weight(nation1, avron));
    assertEquals(NaN, g.weight(avron, nation1));
  }

  @Test
  public void cycleTest() {
    Station psg1      = new Station("PRE SAINT-GERVAIS$1", "7BIS");
    Station psg2      = new Station("PRE SAINT-GERVAIS$2", "7BIS");
    Station pdf       = new Station("PLACE DES FETES", "7BIS");
    Station danube    = new Station("DANUBE", "7BIS");
    Station botzaris1 = new Station("BOTZARIS$1", "7BIS");
    Station botzaris2 = new Station("BOTZARIS$2", "7BIS");
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
    assertEquals(defaultWeight, g.weight(botzaris1, new Station("BUTTES CHAUMONT", "7BIS")));
    assertEquals(defaultWeight, g.weight(botzaris2, new Station("BUTTES CHAUMONT", "7BIS")));
    assertEquals(Double.NaN, g.weight(botzaris1, pdf), 0.0);
    assertEquals(defaultWeight, g.weight(botzaris2, pdf));
    assertEquals(inStationWieght, g.weight(pdf, new Station("PLACE DES FETES", "11")));
    assertEquals(defaultWeight, g.weight(pdf, psg1));
    assertEquals(defaultWeight, g.weight(psg2, danube));
    assertEquals(Double.NaN, g.weight(psg1, danube), 0.0);
    assertEquals(defaultWeight, g.weight(danube, botzaris1));
  }

  @Test
  public void forkTest() {
    Station sdu      = new Station("SAINT-DENIS - UNIVERSITE", "13");
    Station aglc     = new Station("ASNIERES - GENNEVILLIERS - LES COURTILLES", "13");
    Station gm       = new Station("GUY MOQUET", "13");
    Station brochant = new Station("BROCHANT", "13");
    Station lf1       = new Station("LA FOURCHE$1", "13");
    Station lf2       = new Station("LA FOURCHE$2", "13");
    assertEquals(2, g.neighbors(sdu).size());
    assertEquals(2, g.neighbors(aglc).size());
    assertEquals(3, g.neighbors(gm).size());
    assertEquals(3, g.neighbors(brochant).size());
    assertEquals(4, g.neighbors(lf1).size());
    assertEquals(4, g.neighbors(lf2).size());
    assertEquals(defaultWeight, g.weight(sdu, new Station("BASILIQUE DE SAINT-DENIS", "13")));
    assertEquals(defaultWeight, g.weight(aglc, new Station("LES AGNETTES", "13")));
    assertEquals(defaultWeight, g.weight(gm, lf1));
    assertEquals(defaultWeight, g.weight(gm, new Station("PORTE DE SAINT-OUEN", "13")));
    assertEquals(Double.NaN, g.weight(brochant, lf1), 0.0);
    assertEquals(defaultWeight, g.weight(brochant, lf2), 0.0);
    assertEquals(defaultWeight, g.weight(brochant, new Station("PORTE DE CLICHY", "13")));
    assertEquals(Double.NaN, g.weight(lf1, brochant), 0.0);
    assertEquals(defaultWeight, g.weight(lf2, brochant));
    assertEquals(defaultWeight, g.weight(lf1, gm));
    assertEquals(defaultWeight, g.weight(lf1, new Station("PLACE DE CLICHY", "13")));

    HashMap<Station, Double> dist = new HashMap<Station, Double>();
    HashMap<Station, Station> prev = new HashMap<Station, Station>();
    Station ptiS = new Station("PORTE D'ITALIE", "Meta Station Start");
    Station ptiE = new Station("PORTE D'ITALIE", "Meta Station End");
    Station kbS = new Station("LE KREMLIN-BICETRE", "Meta Station Start");
    Station kbE = new Station("LE KREMLIN-BICETRE", "Meta Station End");

    Dijkstra.shortestPath(g, ptiS, prev, dist);
    assertEquals(240.0, dist.get(kbE), 0.0);
    Dijkstra.shortestPath(g, kbS, prev, dist);
    assertEquals(240.0, dist.get(ptiE), 0.0);

  }

  @Test
  public void metaStatTest() {
    Station cStart = new Station("CHATELET", "Meta Station Start");
    Station cEnd   = new Station("CHATELET", "Meta Station End");
    assertEquals(5, g.neighbors(cStart).size());
    assertEquals(0, g.neighbors(cEnd).size());
    assertEquals((Double) 0.0, g.weight(new Station("CHATELET", "4"), cEnd));
    assertEquals((Double) 0.0, g.weight(cStart, new Station("CHATELET", "4")));

  }

}
