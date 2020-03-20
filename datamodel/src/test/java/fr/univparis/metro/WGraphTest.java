package fr.univparis.metro;

import static org.junit.Assert.*;
import java.util.ArrayList;
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
    public void neighborsTest(){
        ArrayList<String> a = new ArrayList<String>();
        a.add("Laumière");
        a.add("Porte de Pantin");
        ArrayList<String> b = new ArrayList<String>();
        b.add("Jaures");
        ArrayList<String> c = new ArrayList<String>();
        c.add("Stade de rugby");
        ArrayList<String> d = new ArrayList<String>();
        d.add("Jaures");
        d.add("Fac des Maths-Info");
        ArrayList<String> e = new ArrayList<String>();
        e.add("Stade de rugby");
        e.add("Rattrapages");
        ArrayList<String> f = new ArrayList<String>();
        f.add("Fac des Maths-Info");
        f.add("Objectif 20/20 au projet");
        ArrayList<String> h = new ArrayList<String>();
        h.add("Rattrapages");
        assertEquals(a, g.neighbors("Ourcq"));
        assertEquals(b, g.neighbors("Laumière"));
        assertEquals(c, g.neighbors("Jaures"));
        assertEquals(d, g.neighbors("Stade de rugby"));
        assertEquals(e, g.neighbors("Fac des Maths-Info"));
        assertEquals(f, g.neighbors("Rattrapages"));
        assertEquals(h, g.neighbors("Objectif 20/20 au projet"));
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
        assertEquals(true, g.getWGraph().containsKey("Ourcq"));
        assertEquals(true, g.getWGraph().containsKey("Laumière")&& g.getWGraph().containsKey("Porte de Pantin"));
        assertEquals(true, g.getWGraph().containsKey("Jaures"));

        assertEquals(false, g.getWGraph().containsKey("Gare du Nord"));
    }

    @Test
    public void deleteVertexTest(){
        g.deleteVertex("Jaures");

        assertEquals(true, g.getWGraph().get("Laumière").isEmpty());
        assertEquals(null, g.getWGraph().get("Jaures"));
    }

    @Test
    public void addEdgeTest(){
        assertEquals("Laumière", g.getWGraph().get("Ourcq").get(0).getObj());
        assertEquals(70.0, g.getWGraph().get("Ourcq").get(0).getValue().doubleValue(), 0.0);
        assertEquals("Jaures", g.getWGraph().get("Laumière").get(0).getObj());
        assertEquals(75.0, g.getWGraph().get("Laumière").get(0).getValue().doubleValue(), 0.0);
    }

    @Test
    public void removeEdgeTest(){
        g.removeEdge("Ourcq", "Laumière");
        g.removeEdge("Ourcq", "Porte de Pantin");
        assertEquals(true, g.getWGraph().get("Ourcq").isEmpty());
    }

    @Test
    public void addDoubleEdgeTest(){
      assertEquals("Ourcq" , g.getWGraph().get("Porte de Pantin").get(0).getObj());
      assertEquals(60.0, g.getWGraph().get("Ourcq").get(1).getValue().doubleValue(), 0.0);
      assertEquals("Porte de Pantin" , g.getWGraph().get("Ourcq").get(1).getObj());
      assertEquals(60.0, g.getWGraph().get("Porte de Pantin").get(0).getValue().doubleValue(), 0.0);
      assertEquals(70.0, g.getWGraph().get("Ourcq").get(0).getValue().doubleValue(), 0.0);
      assertEquals("Jaures", g.getWGraph().get("Stade de rugby").get(0).getObj());
      assertEquals("Stade de rugby", g.getWGraph().get("Jaures").get(0).getObj());
      assertEquals("Fac des Maths-Info", g.getWGraph().get("Stade de rugby").get(1).getObj());
      assertEquals("Stade de rugby", g.getWGraph().get("Fac des Maths-Info").get(0).getObj());
      assertEquals("Rattrapages", g.getWGraph().get("Fac des Maths-Info").get(1).getObj());
      assertEquals("Fac des Maths-Info", g.getWGraph().get("Rattrapages").get(0).getObj());
      assertEquals("Objectif 20/20 au projet", g.getWGraph().get("Rattrapages").get(1).getObj());
      assertEquals("Rattrapages", g.getWGraph().get("Objectif 20/20 au projet").get(0).getObj());
      assertEquals(60.0, g.getWGraph().get("Ourcq").get(1).getValue().doubleValue(), 0.0);
      assertEquals(50.0, g.getWGraph().get("Jaures").get(0).getValue().doubleValue(), 0.0);
      assertEquals(50.0, g.getWGraph().get("Stade de rugby").get(0).getValue().doubleValue(), 0.0);
      assertEquals(100.0, g.getWGraph().get("Stade de rugby").get(1).getValue().doubleValue(), 0.0);
      assertEquals(100.0, g.getWGraph().get("Fac des Maths-Info").get(0).getValue().doubleValue(), 0.0);
      assertEquals(20.0, g.getWGraph().get("Fac des Maths-Info").get(1).getValue().doubleValue(), 0.0);
      assertEquals(20.0, g.getWGraph().get("Rattrapages").get(1).getValue().doubleValue(), 0.0);
      assertEquals(20.0, g.getWGraph().get("Rattrapages").get(0).getValue().doubleValue(), 0.0);
      assertEquals(20.0, g.getWGraph().get("Objectif 20/20 au projet").get(0).getValue().doubleValue(), 0.0);
    }

    @Test
    public void nbVertexTest(){
      assertEquals(8, g.nbVertex(t -> g.getWGraph().get(t).get(0).getValue().doubleValue()!=0));
    }

    @Test
    public void vertexToStringTest(){
      assertEquals("Laumière\nObjectif 20/20 au projet\nRattrapages\nJaures\nFac des Maths-Info\nOurcq\nPorte de Pantin\nStade de rugby\n",
                    g.vertexToString(t -> g.getWGraph().get("Ourcq").get(1).getValue().doubleValue()!=0));
    }
}

