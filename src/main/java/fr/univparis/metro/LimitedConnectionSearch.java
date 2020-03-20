package fr.univparis.metro;

import java.util.LinkedList;

public class LimitedConnectionSearch {

    public static <T> void floydAndVia(Double[][] direct, T[][] via){
        int n = direct.length;
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Double dik = direct[i][k], dkj = direct[k][j];
                    T vkj = via[k][j];
                    if (dik == Double.POSITIVE_INFINITY || dkj == Double.POSITIVE_INFINITY) continue;
                    Double u = dik + dkj;
                    if (u < direct[i][j]){
                        direct[i][j] = u;
                        via[i][j] = vkj;
                    }
                }
            }
        }
    }

    public static  <T extends Object> LinkedList<T> getPath(Double[][] direct, T[][] via, T start, T end){
        LinkedList<T> ret = new LinkedList<T>();
        floydAndVia(direct, via);
        return ret;
    }
}