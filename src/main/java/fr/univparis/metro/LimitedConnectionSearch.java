package fr.univparis.metro;

import java.util.LinkedList;

public class LimitedConnectionSearch {

    /**
     * @param direct is a matrice in which the coefficient direct(x, y) is the time it takes to get to the station n°y from the sation n°x.
     * @param via is a matrice in which via(x, y) is the last station we get through to get to the sation n°y from the station n°x
     * @param intermediate is a matrice in which intermediate(x, y) is the number of station we get through to get to the station n°y from the station n°x
     */
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

    public static  <T> LinkedList<T> getPath(MatriceWGraph g, T start, T end){
        floydAndVia(g.getDirect(), g.getVia(), g.getIntermediate());
        LinkedList<T> ret = new LinkedList<T>();
        return ret;
    }
}