package fr.univparis.metro;

public class LimitedConnectionSearch {

    static int[][] floyd(int[][] c) {
        int n = c.length;
        int[][] d = new int[n][n];
        for (int i=0; i<n; i++) {
          for (int j=0; j<n; j++) {
            d[i][j] = c[i][j];
            }
        }
        for (int k=0; k<n; k++) {
            for (int i=0; i<n; i++) {
                for (int j=0; j<n; j++) {
                    int dik = d[i][k], dkj = d[k][j];
                    if (dik == Integer.MAX_VALUE || dkj == Integer.MAX_VALUE) continue;
                    int u = dik + dkj;
                    if (u < d[i][j]) d[i][j] = u;
                }
            }
        }
        return d;
    }
}