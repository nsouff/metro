package fr.univparis.metro;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class LimitedConnexionSearchTest{

    Double[][] g;

    @Before
    public void initializeGraphMatrice(){
        g = new Double[4][4];

        g[0][0] = 0.0;
        g[0][1] = 2.0;
        g[0][2] = 4.0;
        g[0][3] = Double.POSITIVE_INFINITY;

        g[1][0] = Double.POSITIVE_INFINITY;
        g[1][1] = 0.0;
        g[1][2] = 3.0;
        g[1][3] = Double.POSITIVE_INFINITY;

        g[2][0] = Double.POSITIVE_INFINITY;
        g[2][1] = Double.POSITIVE_INFINITY;
        g[2][2] = 0.0;
        g[2][3] = 5.0;

        g[3][0] = 1.0;
        g[3][1] = Double.POSITIVE_INFINITY;
        g[3][2] = Double.POSITIVE_INFINITY;
        g[3][3] = 0.0;
    }

    @Test
    public void floydTest(){
        Double[][] t = LimitedConnectionSearch.floyd(g, 0, 2);
        Double[][] a = new Double[g.length][g.length];

        a[0][0] = 0.0;
        a[0][1] = 2.0;
        a[0][2] = 4.0;
        a[0][3] = 9.0;

        a[1][0] = 9.0;
        a[1][1] = 0.0;
        a[1][2] = 3.0;
        a[1][3] = 8.0;

        a[2][0] = 6.0;
        a[2][1] = 8.0;
        a[2][2] = 0.0;
        a[2][3] = 5.0;

        a[3][0] = 1.0;
        a[3][1] = 3.0;
        a[3][2] = 5.0;
        a[3][3] = 0.0;

        assertArrayEquals(a, t);
    }
}