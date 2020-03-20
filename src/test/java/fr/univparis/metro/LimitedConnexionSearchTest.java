package fr.univparis.metro;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class LimitedConnexionSearchTest{

    Double[][] graph;
    Integer[][] via;

    @Before
    public void initializeGraphMatrice(){
        graph = new Double[5][5];

        graph[0][0] = 0.0;
        graph[0][1] = 2.0;
        graph[0][2] = 1.0;
        graph[0][3] = Double.POSITIVE_INFINITY;
        graph[0][4] = Double.POSITIVE_INFINITY;

        graph[1][0] = Double.POSITIVE_INFINITY;
        graph[1][1] = 0.0;
        graph[1][2] = 1.0;
        graph[1][3] = 2.0;
        graph[1][4] = 3.0;

        graph[2][0] = Double.POSITIVE_INFINITY;
        graph[2][1] = Double.POSITIVE_INFINITY;
        graph[2][2] = 0.0;
        graph[2][3] = 2.0;
        graph[2][4] = Double.POSITIVE_INFINITY;

        graph[3][0] = 2.0;
        graph[3][1] = Double.POSITIVE_INFINITY;
        graph[3][2] = Double.POSITIVE_INFINITY;
        graph[3][3] = 0.0;
        graph[3][4] = Double.POSITIVE_INFINITY;

        graph[4][0] = Double.POSITIVE_INFINITY;
        graph[4][1] = Double.POSITIVE_INFINITY;
        graph[4][2] = 4.0;
        graph[4][3] = Double.POSITIVE_INFINITY;
        graph[4][4] = 0.0;
    }

    @Before
    public void initializeMatriceVia(){
        via = new Integer[5][5];

        via[0][0] = 1;
        via[0][1] = 1;
        via[0][2] = 1;
        via[0][3] = Integer.MAX_VALUE;
        via[0][4] = Integer.MAX_VALUE;

        via[1][0] = Integer.MAX_VALUE;
        via[1][1] = 2;
        via[1][2] = 2;
        via[1][3] = 2;
        via[1][4] = 2;

        via[2][0] = Integer.MAX_VALUE;
        via[2][1] = Integer.MAX_VALUE;
        via[2][2] = 3;
        via[2][3] = 3;
        via[2][4] = Integer.MAX_VALUE;

        via[3][0] = 4;
        via[3][1] = Integer.MAX_VALUE;
        via[3][2] = Integer.MAX_VALUE;
        via[3][3] = 4;
        via[3][4] = Integer.MAX_VALUE;

        via[4][0] = Integer.MAX_VALUE;
        via[4][1] = Integer.MAX_VALUE;
        via[4][2] = 5;
        via[4][3] = Integer.MAX_VALUE;
        via[4][4] = 5;
    }

    @Test
    public void floydTest(){
        LimitedConnectionSearch.floydAndVia(graph, via);

        Double[][] b = new Double[graph.length][graph.length];

        b[0][0] = 0.0;
        b[0][1] = 2.0;
        b[0][2] = 1.0;
        b[0][3] = 3.0;
        b[0][4] = 5.0;

        b[1][0] = 4.0;
        b[1][1] = 0.0;
        b[1][2] = 1.0;
        b[1][3] = 2.0;
        b[1][4] = 3.0;

        b[2][0] = 4.0;
        b[2][1] = 6.0;
        b[2][2] = 0.0;
        b[2][3] = 2.0;
        b[2][4] = 9.0;

        b[3][0] = 2.0;
        b[3][1] = 4.0;
        b[3][2] = 3.0;
        b[3][3] = 0.0;
        b[3][4] = 7.0;

        b[4][0] = 8.0;
        b[4][1] = 10.0;
        b[4][2] = 4.0;
        b[4][3] = 6.0;
        b[4][4] = 0.0;

        Integer[][] v = new Integer[via.length][via.length];

        v[0][0] = 1;
        v[0][1] = 1;
        v[0][2] = 1;
        v[0][3] = 3;
        v[0][4] = 2;

        v[1][0] = 4;
        v[1][1] = 2;
        v[1][2] = 2;
        v[1][3] = 2;
        v[1][4] = 2;

        v[2][0] = 4;
        v[2][1] = 1;
        v[2][2] = 3;
        v[2][3] = 3;
        v[2][4] = 2;

        v[3][0] = 4;
        v[3][1] = 1;
        v[3][2] = 1;
        v[3][3] = 4;
        v[3][4] = 2;

        v[4][0] = 4;
        v[4][1] = 1;
        v[4][2] = 5;
        v[4][3] = 3;
        v[4][4] = 5;

        assertArrayEquals(b, graph);
        assertArrayEquals(v, via);
    }
}