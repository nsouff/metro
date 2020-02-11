package fr.univparis.metro;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class WGraphTest{

    @Test
    public void neighborsTest(){
        WGraph<String> g = WGraph.createGraph();
        g.addVertex("Ourcq");
        g.addVertex("Laumière");
        g.addVertex("Jaures");

        g.addEdge("Ourcq", "Laumière", 70.0);
        g.addEdge("Laumière", "Jaures", 75.0);

        ArrayList<String> a = new ArrayList<String>();
        a.add("Laumière");
        ArrayList<String> b = new ArrayList<String>();
        b.add("Jaures");
        assertEquals(a, g.neighbors("Ourcq"));
        assertEquals(b, g.neighbors("Laumière"));
    }

    @Test
    public void weightTest(){
        WGraph<String> g = WGraph.createGraph();
        g.addVertex("Ourcq");
        g.addVertex("Laumière");
        g.addVertex("Jaures");

        g.addEdge("Ourcq", "Laumière", 70.0);
        g.addEdge("Laumière", "Jaures", 75.0);

        assertEquals(70.0, g.weight("Ourcq", "Laumière"), 0.0);
        assertEquals(75.0, g.weight("Laumière", "Jaures"), 0.0);
    }

    @Test
    public void addVertexTest(){
        WGraph<String> g = WGraph.createGraph();
        g.addVertex("Ourcq");
        g.addVertex("Laumière");
        g.addVertex("Jaurès");

        assertEquals(true, g.getWGraph().containsKey("Ourcq"));
        assertEquals(true, g.getWGraph().containsKey("Laumière"));
        assertEquals(true, g.getWGraph().containsKey("Jaurès"));
        assertEquals(false, g.getWGraph().containsKey("Gare du Nord"));
    }

    @Test
    public void deleteVertexTest(){
        WGraph<String> g = WGraph.createGraph();
        g.addVertex("Ourcq");
        g.addVertex("Laumière");
        g.addVertex("Jaures");

        g.addEdge("Ourcq", "Laumière", 70.0);
        g.addEdge("Laumière", "Jaures", 75.0);

        assertEquals(true, g.deleteVertex("Jaures"));

    }

    @Test
    public void addEdgeTest(){
        WGraph<String> g = WGraph.createGraph();
        g.addVertex("Ourcq");
        g.addVertex("Laumière");
        g.addVertex("Jaures");

        g.addEdge("Ourcq", "Laumière", 70.0);
        g.addEdge("Laumière", "Jaures", 75.0);

        assertEquals("Laumière", g.getWGraph().get("Ourcq").get(0).getVertex());
        assertEquals(70.0, g.getWGraph().get("Ourcq").get(0).getWeight().doubleValue(), 0.0);
        assertEquals("Jaures", g.getWGraph().get("Laumière").get(0).getVertex());
        assertEquals(75.0, g.getWGraph().get("Laumière").get(0).getWeight().doubleValue(), 0.0);
    }

    @Test
    public void removeEdgeTest(){
        WGraph<String> g = WGraph.createGraph();
        g.addVertex("Ourcq");
        g.addVertex("Laumière");
        g.addVertex("Jaures");

        g.addEdge("Ourcq", "Laumière", 70.0);
        g.addEdge("Laumière", "Jaures", 75.0);

        g.removeEdge("Ourcq", "Laumière");

        assertEquals(true, g.getWGraph().get("Ourcq").isEmpty());
    }

}
