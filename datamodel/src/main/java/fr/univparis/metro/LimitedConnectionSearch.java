package fr.univparis.metro;

import java.util.ArrayList;

public class LimitedConnectionSearch {

    /**
     * @param direct is a matrice in which the coefficient direct(x, y) is the time it takes to get to the station n°y from the sation n°x.
     * @param via is a matrice in which via(x, y) is the last station we get through to get to the sation n°y from the station n°x
     * @param intermediate is a matrice in which intermediate(x, y) is the number of station we get through to get to the station n°y from the station n°x
     */
    public static <T> void floyd(Double[][] direct, T[][] via, Integer[][] intermediate){
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
                    if (v < intermediate[i][j]){
                        direct[i][j] = u;
                        via[i][j] = vkj;
                        intermediate[i][j] = v;
                    }
                    else if(v == intermediate[i][j] && u < direct[i][j]){
                        direct[i][j] = u;
                        via[i][j] = vkj;
                        intermediate[i][j] = v;
                    }
                }
            }
        }
    }

    public static Double[][] floyd(Double[][] d){
        int n = d.length;
        Double[][] ret = new Double[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                ret[i][j] = d[i][j];
            }
        }
        for(int k = 0; k < n; k++){
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    Double dik = ret[i][k];
                    Double dkj = ret[j][k];
                    if(dik == Double.POSITIVE_INFINITY || dkj == Double.POSITIVE_INFINITY) continue;
                    Double u = dik + dkj;
                    if(u < ret[i][j]) ret[i][j] = u;
                }
            }
        }
        return ret;
    }

        public static ArrayList<Pair<String, String>> getPath(MatriceWGraph g, String start, String end){
        floyd(g.getDirect(), g.getVia(), g.getIntermediate());
        int numStart = g.getSetOfVertices().get(start);
        int numEnd = g.getSetOfVertices().get(end);
        ArrayList<Pair<String, String>> ret = new ArrayList<Pair<String, String>>();
        String current = end;
        String currentLine = "FIN";
        ret.add(new Pair<String, String>(current, currentLine));
        int n = numEnd;
        while(!start.equals(current)){
            current = g.getVia()[numStart][n].getName();
            currentLine = g.getVia()[numStart][n].getLine();
            Pair<String, String> p = new Pair<String, String>(current, currentLine);
            ret.add(p);
            n = g.getSetOfVertices().get(current);
        }
        return ret;
    }
}
