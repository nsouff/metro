package fr.univparis.metro;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class WGraphTest{

  WGraph<String> g;

  @Before
  public void initializeGraph(){
    g = new WGraph<String>();
    g.addVertex("Ourcq");
    g.addVertex("Laumière");
    g.addVertex("Jaures");
    g.addVertex("Porte de Pantin");
    g.addVertex("Fac des Maths-Info");
    g.addVertex("Stade de rugby");
    g.addVertex("Rattrapages");
    g.addVertex("Objectif 20/20 au projet");
    g.addEdge("Ourcq", "Laumière", 70.0);
    g.addEdge("Laumière", "Jaures", 75.0);
    g.addDoubleEdge("Ourcq", 60., (t -> t.equals("Porte de Pantin")));
    g.addDoubleEdge("Jaures", 50., (t -> t.equals("Stade de rugby")));
    g.addDoubleEdge("Stade de rugby", 100., (t -> t.equals("Fac des Maths-Info")));
    g.addDoubleEdge("Fac des Maths-Info", 20., (t -> t.equals("Rattrapages")));
    g.addDoubleEdge("Rattrapages", 20., (t -> t.equals("Objectif 20/20 au projet")));
  }

  @Test
  public void cloneTest(){
    WGraph<String> h = g.clone();
    assertTrue(g.equals(h));
    assertTrue(g.neighbors("Ourcq").contains("Laumière"));
    assertTrue(h.neighbors("Ourcq").contains("Laumière"));
    h.removeEdge("Ourcq", "Laumière");
    assertTrue(g.neighbors("Ourcq").contains("Laumière"));
    assertFalse(h.neighbors("Ourcq").contains("Laumière"));
  }

  @Test
  public void weightTest(){
    assertEquals(70.0, g.weight("Ourcq", "Laumière"), 0.0);
    assertEquals(75.0, g.weight("Laumière", "Jaures"), 0.0);
    assertEquals(60.0, g.weight("Ourcq", "Porte de Pantin"), 0.0);
    assertEquals(60.0, g.weight("Porte de Pantin", "Ourcq"), 0.0);
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
    assertEquals(true, g.containsVertex("Ourcq"));
    assertEquals(true, g.containsVertex("Laumière")&& g.containsVertex("Porte de Pantin"));
    assertEquals(true, g.containsVertex("Jaures"));

    assertEquals(false, g.containsVertex("Gare du Nord"));
  }

  @Test
  public void deleteVertexTest() {
    g.deleteVertex("Jaures");

    assertFalse(g.containsVertex("Jaures"));
    assertEquals(Double.NaN, g.weight("Laumière", "Jaures"), 0.0);
  }

  @Test
  public void addEdgeTest() {
    assertTrue(g.neighbors("Ourcq").contains("Laumière"));
    assertTrue(g.neighbors("Laumière").contains("Jaures"));
  }

  @Test
  public void removeEdgeTest(){
    g.removeEdge("Ourcq", "Laumière");
    g.removeEdge("Ourcq", "Porte de Pantin");
    assertTrue(g.neighbors("Ourcq").isEmpty());
  }

  @Test
  public void addDoubleEdgeTest(){

    // Value of the weight are tested on weightTest

    assertTrue(g.neighbors("Porte de Pantin").contains("Ourcq"));
    assertTrue(g.neighbors("Ourcq").contains("Porte de Pantin"));

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
    other.addVertex("Bastille");
    other.addVertex("Ourcq");
    other.addVertex("Porte de Pantin");
    other.addEdge("Bastille", "Ourcq", 21.0);
    other.addEdge("Porte de Pantin", "Bastille", 5.0);
    other.addEdge("Ourcq", "Porte de Pantin", 10.0);

    g.apply(other);
    assertTrue(g.containsVertex("Bastille"));
    assertEquals(21.0, g.weight("Bastille", "Ourcq"), 0.0);
    assertEquals(Double.NaN, g.weight("Ourcq", "Bastille"), 0.0);
    assertEquals(60.0, g.weight("Porte de Pantin", "Ourcq"), 0.0);
    assertEquals(10.0, g.weight("Ourcq", "Porte de Pantin"), 0.0);
    assertEquals(1, g.neighbors("Bastille").size());
    assertEquals(10.0, g.weight("Ourcq", "Porte de Pantin"), 0.0);
    assertEquals(5.0, g.weight("Porte de Pantin", "Bastille"), 0.0);

    // other not modified test
    assertEquals(3, other.nbVertex());
    assertEquals(21.0, other.weight("Bastille", "Ourcq"), 0.0);
    assertEquals(5.0, other.weight("Porte de Pantin", "Bastille"), 0.0);
    assertEquals(10.0, other.weight("Ourcq", "Porte de Pantin"), 0.0);
  }

  @Test
  public void splitVertexTest() {
    String b = "Laumière";
    String split1 = "Laumière1";
    String split2 = "Laumière2";
    g.splitVertex(b, split1, split2);

    assertFalse(g.containsVertex(b));
    assertTrue(g.containsVertex(split1));
    assertTrue(g.containsVertex(split2));
    assertEquals(70.0, g.weight("Ourcq", split1), 0.0);
    assertEquals(70.0, g.weight("Ourcq", split2), 0.0);
    assertEquals(75.0, g.weight(split1, "Jaures"), 0.0);
    assertEquals(75.0, g.weight(split2, "Jaures"), 0.0);

  }

}
