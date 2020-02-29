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
        g.addEdge("Ourcq", "Laumière", 70.0);
        g.addEdge("Laumière", "Jaures", 75.0);
    }

    @Test
    public void neighborsTest(){
        ArrayList<String> a = new ArrayList<String>();
        a.add("Laumière");
        ArrayList<String> b = new ArrayList<String>();
        b.add("Jaures");
        assertEquals(a, g.neighbors("Ourcq"));
        assertEquals(b, g.neighbors("Laumière"));
    }

    @Test
    public void weightTest(){
        assertEquals(70.0, g.weight("Ourcq", "Laumière"), 0.0);
        assertEquals(75.0, g.weight("Laumière", "Jaures"), 0.0);
    }

    @Test
    public void addVertexTest(){
        assertEquals(true, g.getWGraph().containsKey("Ourcq"));
        assertEquals(true, g.getWGraph().containsKey("Laumière"));
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

        assertEquals(true, g.getWGraph().get("Ourcq").isEmpty());
    }

}
