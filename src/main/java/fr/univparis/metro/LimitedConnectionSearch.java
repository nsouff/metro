package fr.univparis.metro;

import java.util.LinkedList;

public class LimitedConnectionSearch {

    public static <T> void floydAndVia(Double[][] direct, T[][] via, Integer[][] intermediate){
        int n = direct.length;
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Double dik = direct[i][k];
                    Double dkj = direct[k][j];
                    Integer intermediateIK = intermediate[i][k];
                    Integer intermediateKJ = intermediate[k][j];
                    T vkj = via[k][j];
                    if (dik == Double.POSITIVE_INFINITY || dkj == Double.POSITIVE_INFINITY) continue;
                    Double u = dik + dkj;
                    int v = intermediateIK + intermediateKJ;
                    if (u < direct[i][j] && v <= intermediate[i][j]){
                        direct[i][j] = u;
                        via[i][j] = vkj;
                        intermediate[i][j] = v;
                    }
                }
            }
        }
    }

    public static  <T extends Object> LinkedList<T> getPath(Double[][] direct, T[][] via, Integer[][] intermediate, T start, T end){
        LinkedList<T> ret = new LinkedList<T>();
        floydAndVia(direct, via, intermediate);
        return ret;
    }
}