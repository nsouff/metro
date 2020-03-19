package fr.univparis.metro;

public class LimitedConnectionSearch {

    public static Double[][] floyd(Double[][] graph, int start, int end){
        int n = graph.length;
        Double[][] d = new Double[n][n];
        for (int i = 0; i < n; i++) {
          for (int j = 0; j < n; j++) {
            d[i][j] = graph[i][j];
            }
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Double dik = d[i][k], dkj = d[k][j];
                    if (dik == Double.POSITIVE_INFINITY || dkj == Double.POSITIVE_INFINITY) continue;
                    Double u = dik + dkj;
                    if (u < d[i][j]) d[i][j] = u;
                }
            }
        }
        return d;
    }
}