package fr.univparis.metro;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class LimitedConnexionSearchTest{

    Double[][] graph;
    Integer[][] via;
    Integer[][] intermediate;

    @Before
    public void initializeGraphMatrice(){
        graph = new Double[6][6];

        graph[0][0] = 0.0;
        graph[0][1] = 2.0;
        graph[0][2] = 1.0;
        graph[0][3] = Double.POSITIVE_INFINITY;
        graph[0][4] = Double.POSITIVE_INFINITY;
        graph[0][5] = 10.0;

        graph[1][0] = Double.POSITIVE_INFINITY;
        graph[1][1] = 0.0;
        graph[1][2] = 1.0;
        graph[1][3] = 2.0;
        graph[1][4] = 3.0;
        graph[1][5] = Double.POSITIVE_INFINITY;

        graph[2][0] = Double.POSITIVE_INFINITY;
        graph[2][1] = Double.POSITIVE_INFINITY;
        graph[2][2] = 0.0;
        graph[2][3] = 2.0;
        graph[2][4] = Double.POSITIVE_INFINITY;
        graph[2][5] = Double.POSITIVE_INFINITY;

        graph[3][0] = 2.0;
        graph[3][1] = Double.POSITIVE_INFINITY;
        graph[3][2] = Double.POSITIVE_INFINITY;
        graph[3][3] = 0.0;
        graph[3][4] = Double.POSITIVE_INFINITY;
        graph[3][5] = Double.POSITIVE_INFINITY;

        graph[4][0] = Double.POSITIVE_INFINITY;
        graph[4][1] = Double.POSITIVE_INFINITY;
        graph[4][2] = 4.0;
        graph[4][3] = Double.POSITIVE_INFINITY;
        graph[4][4] = 0.0;
        graph[4][5] = 1.0;

        graph[5][0] = Double.POSITIVE_INFINITY;
        graph[5][1] = Double.POSITIVE_INFINITY;
        graph[5][2] = Double.POSITIVE_INFINITY;
        graph[5][3] = Double.POSITIVE_INFINITY;
        graph[5][4] = Double.POSITIVE_INFINITY;
        graph[5][5] = 0.0;
    }

    @Before
    public void initializeViaMatrice(){
        via = new Integer[6][6];

        via[0][0] = 1;
        via[0][1] = 1;
        via[0][2] = 1;
        via[0][3] = null;
        via[0][4] = null;
        via[0][5] = 1;

        via[1][0] = null;
        via[1][1] = 2;
        via[1][2] = 2;
        via[1][3] = 2;
        via[1][4] = 2;
        via[1][5] = null;

        via[2][0] = null;
        via[2][1] = null;
        via[2][2] = 3;
        via[2][3] = 3;
        via[2][4] = null;
        via[2][5] = null;

        via[3][0] = 4;
        via[3][1] = null;
        via[3][2] = null;
        via[3][3] = 4;
        via[3][4] = null;
        via[3][5] = null;

        via[4][0] = null;
        via[4][1] = null;
        via[4][2] = 5;
        via[4][3] = null;
        via[4][4] = 5;
        via[4][5] = 5;

        via[5][0] = null;
        via[5][1] = null;
        via[5][2] = null;
        via[5][3] = null;
        via[5][4] = null;
        via[5][5] = 6;
    }

    @Before
    public void initializeIntermediateMatrice(){
        intermediate = new Integer[6][6];

        intermediate[0][0] = 0;
        intermediate[0][1] = 1;
        intermediate[0][2] = 1;
        intermediate[0][3] = Integer.MAX_VALUE;
        intermediate[0][4] = Integer.MAX_VALUE;
        intermediate[0][5] = 1;

        intermediate[1][0] = Integer.MAX_VALUE;
        intermediate[1][1] = 0;
        intermediate[1][2] = 1;
        intermediate[1][3] = 1;
        intermediate[1][4] = 1;
        intermediate[1][5] = Integer.MAX_VALUE;

        intermediate[2][0] = Integer.MAX_VALUE;
        intermediate[2][1] = Integer.MAX_VALUE;
        intermediate[2][2] = 0;
        intermediate[2][3] = 1;
        intermediate[2][4] = Integer.MAX_VALUE;
        intermediate[2][5] = Integer.MAX_VALUE;

        intermediate[3][0] = 1;
        intermediate[3][1] = Integer.MAX_VALUE;
        intermediate[3][2] = Integer.MAX_VALUE;
        intermediate[3][3] = 0;
        intermediate[3][4] = Integer.MAX_VALUE;
        intermediate[3][5] = Integer.MAX_VALUE;

        intermediate[4][0] = Integer.MAX_VALUE;
        intermediate[4][1] = Integer.MAX_VALUE;
        intermediate[4][2] = 1;
        intermediate[4][3] = Integer.MAX_VALUE;
        intermediate[4][4] = 0;
        intermediate[4][5] = 1;

        intermediate[5][0] = Integer.MAX_VALUE;
        intermediate[5][1] = Integer.MAX_VALUE;
        intermediate[5][2] = Integer.MAX_VALUE;
        intermediate[5][3] = Integer.MAX_VALUE;
        intermediate[5][4] = Integer.MAX_VALUE;
        intermediate[5][5] = 0;
    }

    @Test
    public void floydTest(){
        LimitedConnectionSearch.floydAndVia(graph, via, intermediate);

        Double[][] b = new Double[graph.length][graph.length];

        b[0][0] = 0.0;
        b[0][1] = 2.0;
        b[0][2] = 1.0;
        b[0][3] = 3.0;
        b[0][4] = 5.0;
        b[0][5] = 10.0;

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
    }
}