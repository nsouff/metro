package fr.univparis.metro;

import java.util.ArrayList;
import java.util.HashMap;

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
        HashMap<String, Integer> h = g.getForkAndCycleStation();
        if(h.containsKey(start)) return getPathForkCycleStart(g, start, end);
        if(h.containsKey(end)) return getPathForkCycleEnd(g, start, end);
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


//The following methods are used to fix the bug of the correspondance times when we go through a fork or a cycle

    private static ArrayList<Pair<String, String>> getPathForkCycleStart(MatriceWGraph g, String start, String end){
        String str1 = start + "$1";
        String str2 = start + "$2";
        ArrayList<Pair<String, String>> a1 = new ArrayList<Pair<String, String>>();
        int numStart1 = g.getSetOfVertices().get(str1);
        int numEnd1 = g.getSetOfVertices().get(end);
        String current1 = end;
        String currentLine1 = "FIN";
        a1.add(new Pair<String, String>(current1, currentLine1));
        int n1 = numEnd1;
        while(!str1.equals(current1)){
            current1 = g.getVia()[numStart1][n1].getName();
            currentLine1 = g.getVia()[numStart1][n1].getLine();
            Pair<String, String> p = new Pair<String, String>(current1, currentLine1);
            a1.add(p);
            n1 = g.getSetOfVertices().get(current1);
        }
        ArrayList<Pair<String, String>> a2 = new ArrayList<Pair<String, String>>();
        int numStart2 = g.getSetOfVertices().get(str2);
        int numEnd2 = g.getSetOfVertices().get(end);
        String current2 = end;
        String currentLine2 = "FIN";
        a2.add(new Pair<String, String>(current2, currentLine2));
        int n2 = numEnd2;
        while(!str2.equals(current2)){
            current2 = g.getVia()[numStart2][n2].getName();
            currentLine2 = g.getVia()[numStart2][n2].getLine();
            Pair<String, String> p = new Pair<String, String>(current2, currentLine2);
            a2.add(p);
            n2 = g.getSetOfVertices().get(current2);
        }
        return (a1.size() < a2.size()) ? a1 : a2;
    }

    private static ArrayList<Pair<String, String>> getPathForkCycleEnd(MatriceWGraph g, String start, String end){
        String str1 = end + "$1";
        String str2 = end + "$2";
        ArrayList<Pair<String, String>> a1 = new ArrayList<Pair<String, String>>();
        int numStart1 = g.getSetOfVertices().get(start);
        int numEnd1 = g.getSetOfVertices().get(str1);
        String current1 = end;
        String currentLine1 = "FIN";
        a1.add(new Pair<String, String>(current1, currentLine1));
        int n1 = numEnd1;
        while(!start.equals(current1)){
            current1 = g.getVia()[numStart1][n1].getName();
            currentLine1 = g.getVia()[numStart1][n1].getLine();
            Pair<String, String> p = new Pair<String, String>(current1, currentLine1);
            a1.add(p);
            n1 = g.getSetOfVertices().get(current1);
        }
        ArrayList<Pair<String, String>> a2 = new ArrayList<Pair<String, String>>();
        int numStart2 = g.getSetOfVertices().get(start);
        int numEnd2 = g.getSetOfVertices().get(str2);
        String current2 = end;
        String currentLine2 = "FIN";
        a2.add(new Pair<String, String>(current2, currentLine2));
        int n2 = numEnd2;
        while(!start.equals(current2)){
            current2 = g.getVia()[numStart2][n2].getName();
            currentLine2 = g.getVia()[numStart2][n2].getLine();
            Pair<String, String> p = new Pair<String, String>(current2, currentLine2);
            a2.add(p);
            n2 = g.getSetOfVertices().get(current2);
        }
        return (a1.size() < a2.size()) ? a1 : a2;
    }
}