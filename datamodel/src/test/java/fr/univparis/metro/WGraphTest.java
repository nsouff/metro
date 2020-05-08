package fr.univparis.metro;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class WGraphTest{

  WGraph<String> g;

  @Before
  public void initializeGraph(){
    g = new WGraph<String>();
    g.addVertex("OURCQ");
    g.addVertex("LAUMIERE");
    g.addVertex("Jaures");
    g.addVertex("PORTE DE PANTIN");
    g.addVertex("Fac des Maths-Info");
    g.addVertex("Stade de rugby");
    g.addVertex("Rattrapages");
    g.addVertex("Objectif 20/20 au projet");
    g.addEdge("OURCQ", "LAUMIERE", 70.0);
    g.addEdge("LAUMIERE", "Jaures", 75.0);
    g.addDoubleEdge("OURCQ", 60., (t -> t.equals("PORTE DE PANTIN")));
    g.addDoubleEdge("Jaures", 50., (t -> t.equals("Stade de rugby")));
    g.addDoubleEdge("Stade de rugby", 100., (t -> t.equals("Fac des Maths-Info")));
    g.addDoubleEdge("Fac des Maths-Info", 20., (t -> t.equals("Rattrapages")));
    g.addDoubleEdge("Rattrapages", 20., (t -> t.equals("Objectif 20/20 au projet")));
  }

  @Test
  public void cloneTest(){
    WGraph<String> h = g.clone();
    assertTrue(g.equals(h));
    assertTrue(g.neighbors("OURCQ").contains("LAUMIERE"));
    assertTrue(h.neighbors("OURCQ").contains("LAUMIERE"));
    h.removeEdge("OURCQ", "LAUMIERE");
    assertTrue(g.neighbors("OURCQ").contains("LAUMIERE"));
    assertFalse(h.neighbors("OURCQ").contains("LAUMIERE"));
  }

  @Test
  public void weightTest(){
    assertEquals(70.0, g.weight("OURCQ", "LAUMIERE"), 0.0);
    assertEquals(75.0, g.weight("LAUMIERE", "Jaures"), 0.0);
    assertEquals(60.0, g.weight("OURCQ", "PORTE DE PANTIN"), 0.0);
    assertEquals(60.0, g.weight("PORTE DE PANTIN", "OURCQ"), 0.0);
    assertEquals(50.0, g.weight("Jaures", "Stade de rugby"), 0.0);
    assertEquals(50.0, g.weight("Stade de rugby", "Jaures"), 0.0);
    assertEquals(100.0, g.weight("Stade de rugby", "Fac des Maths-Info"), 0.0);
    assertEquals(100.0, g.weight("Fac des Maths-Info", "Stade de rugby"), 0.0);
    assertEquals(20.0, g.weight("Rattrapages", "Fac des Maths-Info"), 0.0);
    assertEquals(20.0, g.weight("Fac des Maths-Info", "Rattrapages"), 0.0);
    assertEquals(20.0, g.weight("Rattrapages", "Objectif 20/20 au projet"), 0.0);
    assertEquals(20.0, g.weight("Objectif 20/20 au projet", "Rattrapages"), 0.0);
  }

  @Test
  public void addVertexTest(){
    assertEquals(true, g.containsVertex("OURCQ"));
    assertEquals(true, g.containsVertex("LAUMIERE")&& g.containsVertex("PORTE DE PANTIN"));
    assertEquals(true, g.containsVertex("Jaures"));

    assertEquals(false, g.containsVertex("Gare du Nord"));
  }

  @Test
  public void deleteVertexTest() {
    g.deleteVertex("Jaures");

    assertFalse(g.containsVertex("Jaures"));
    assertEquals(Double.NaN, g.weight("LAUMIERE", "Jaures"), 0.0);
  }

  @Test
  public void addEdgeTest() {
    assertTrue(g.neighbors("OURCQ").contains("LAUMIERE"));
    assertTrue(g.neighbors("LAUMIERE").contains("Jaures"));
  }

  @Test
  public void removeEdgeTest(){
    g.removeEdge("OURCQ", "LAUMIERE");
    g.removeEdge("OURCQ", "PORTE DE PANTIN");
    assertTrue(g.neighbors("OURCQ").isEmpty());
  }

  @Test
  public void addDoubleEdgeTest(){

    // Value of the weight are tested on weightTest

    assertTrue(g.neighbors("PORTE DE PANTIN").contains("OURCQ"));
    assertTrue(g.neighbors("OURCQ").contains("PORTE DE PANTIN"));

    assertTrue(g.neighbors("Jaures").contains("Stade de rugby"));
    assertTrue(g.neighbors("Stade de rugby").contains("Jaures"));

    assertTrue(g.neighbors("Fac des Maths-Info").contains("Stade de rugby"));
    assertTrue(g.neighbors("Stade de rugby").contains("Fac des Maths-Info"));

    assertTrue(g.neighbors("Jaures").contains("Stade de rugby"));
    assertTrue(g.neighbors("Stade de rugby").contains("Jaures"));

    assertTrue(g.neighbors("Fac des Maths-Info").contains("Rattrapages"));
    assertTrue(g.neighbors("Rattrapages").contains("Fac des Maths-Info"));


  }

  @Test
  public void nbVertexTest(){
    assertEquals(2, g.nbVertex(t -> t.startsWith("O")));
  }

  @Test
  public void applyTest() {
    WGraph<String> other = new WGraph<String>();
    other.addVertex("BASTILLE");
    other.addVertex("OURCQ");
    other.addVertex("PORTE DE PANTIN");
    other.addEdge("BASTILLE", "OURCQ", 21.0);
    other.addEdge("PORTE DE PANTIN", "BASTILLE", 5.0);
    other.addEdge("OURCQ", "PORTE DE PANTIN", 10.0);

    g.apply(other);
    assertTrue(g.containsVertex("BASTILLE"));
    assertEquals(21.0, g.weight("BASTILLE", "OURCQ"), 0.0);
    assertEquals(Double.NaN, g.weight("OURCQ", "BASTILLE"), 0.0);
    assertEquals(60.0, g.weight("PORTE DE PANTIN", "OURCQ"), 0.0);
    assertEquals(10.0, g.weight("OURCQ", "PORTE DE PANTIN"), 0.0);
    assertEquals(1, g.neighbors("BASTILLE").size());
    assertEquals(10.0, g.weight("OURCQ", "PORTE DE PANTIN"), 0.0);
    assertEquals(5.0, g.weight("PORTE DE PANTIN", "BASTILLE"), 0.0);

    // other not modified test
    assertEquals(3, other.nbVertex());
    assertEquals(21.0, other.weight("BASTILLE", "OURCQ"), 0.0);
    assertEquals(5.0, other.weight("PORTE DE PANTIN", "BASTILLE"), 0.0);
    assertEquals(10.0, other.weight("OURCQ", "PORTE DE PANTIN"), 0.0);
  }

  @Test
  public void splitVertexTest() {
    String b = "LAUMIERE";
    String split1 = "LAUMIERE1";
    String split2 = "LAUMIERE2";
    g.splitVertex(b, split1, split2);

    assertFalse(g.containsVertex(b));
    assertTrue(g.containsVertex(split1));
    assertTrue(g.containsVertex(split2));
    assertEquals(70.0, g.weight("OURCQ", split1), 0.0);
    assertEquals(70.0, g.weight("OURCQ", split2), 0.0);
    assertEquals(75.0, g.weight(split1, "Jaures"), 0.0);
    assertEquals(75.0, g.weight(split2, "Jaures"), 0.0);

  }

}
