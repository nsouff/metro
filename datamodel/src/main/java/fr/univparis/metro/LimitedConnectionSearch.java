package fr.univparis.metro;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An implemetation of Floyd's algorithm to find an itinerary that limits the number of correspondances.
 */
public class LimitedConnectionSearch {

    /**
     * Execute Floyd's algorithm on graph represented by matrices to determine the time and number of correspondances of any itinerary.
     * @param direct is a matrice in which the coefficient direct(x, y) is the time it takes to get to the station n°y from the sation n°x.
     * @param via is a matrice in which via(x, y) is the last station we get through to get to the sation n°y from the station n°x.
     * @param intermediate is a matrice in which intermediate(x, y) is the number of correspondances to get to the station n°y from the station n°x.
     * @param <T> the type of the vertices (the stations).
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

    /**
     * this method is used to build the graphs of each lines in the MatriceWGraph class. It determines the time of the itinerary between two stations that are on the same line.
     * @param d is a matrice in which the coefficient direct(x, y) is the time it takes to get to the station n°y from the sation n°x.
     * @return A copy of the matrice d, on which Floyd's algorithm is executed.
     */
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
                    Double dkj = ret[k][j];
                    if(dik == Double.POSITIVE_INFINITY || dkj == Double.POSITIVE_INFINITY) continue;
                    Double u = dik + dkj;
                    if(u < ret[i][j]) ret[i][j] = u;
                }
            }
        }
        return ret;
    }

    /**
     * Store the itinerary to get from a station to an other in a arraylist of string, each string being a name of a station.
     * @param g the graph of the whole subway network.
     * @param start the name of the station of which we leave.
     * @param end the name of the arrival station.
     * @return an arraylist containing the path to get from start to end.
     */
    public static ArrayList<Pair<String, String>> getPath(MatriceWGraph g, String start, String end){
        HashMap<String, Integer> h = g.getForkAndCycleStation();
        String strStart = start;
        String strEnd = end;
        if(h.containsKey(start) && h.containsKey(end)){
            Pair<String, String> p = startAndEnd(g, start, end);
            strStart = p.getObj();
            strEnd = p.getValue();
        }
        if(h.containsKey(start) && !h.containsKey(end)) strStart = start(g, start, end);
        if(h.containsKey(end) && !h.containsKey(start)) strEnd = end(g, start, end);
        int numStart = g.getSetOfVertices().get(strStart);
        int numEnd = g.getSetOfVertices().get(strEnd);
        ArrayList<Pair<String, String>> ret = new ArrayList<Pair<String, String>>();
        String current = strEnd;
        String currentLine = "FIN";
        ret.add(new Pair<String, String>(current, currentLine));
        int n = numEnd;
        while(!strStart.equals(current)){
            //System.out.println(current);
            current = g.getVia()[numStart][n].getName();
            currentLine = g.getVia()[numStart][n].getLine();
            Pair<String, String> p = new Pair<String, String>(current, currentLine);
            ret.add(p);
            n = g.getSetOfVertices().get(current);
        }
        return ret;
    }


//The following methods are used to avoid typing nameStation + $1-2 when the departure or the arrival is the begining
//of a fork or a cycle
    private static String start(MatriceWGraph g, String start, String end){
        String str1 = start + "$1";
        String str2 = start + "$2";
        int nbCorrespondances1 = g.getIntermediate()[g.getSetOfVertices().get(str1)][g.getSetOfVertices().get(end)];
        Double time1 = g.getDirect()[g.getSetOfVertices().get(str1)][g.getSetOfVertices().get(end)];
        int nbCorrespondances2 = g.getIntermediate()[g.getSetOfVertices().get(str2)][g.getSetOfVertices().get(end)];
        Double time2 = g.getDirect()[g.getSetOfVertices().get(str2)][g.getSetOfVertices().get(end)];
        if(nbCorrespondances1 < nbCorrespondances2 || (nbCorrespondances1 == nbCorrespondances2 && time1 < time2)) return str1;
        else return str2;
    }

    private static String end(MatriceWGraph g, String start, String end){
        String str1 = end + "$1";
        String str2 = end + "$2";
        int nbCorrespondances1 = g.getIntermediate()[g.getSetOfVertices().get(start)][g.getSetOfVertices().get(str1)];
        Double time1 = g.getDirect()[g.getSetOfVertices().get(start)][g.getSetOfVertices().get(str1)];
        int nbCorrespondances2 = g.getIntermediate()[g.getSetOfVertices().get(start)][g.getSetOfVertices().get(str2)];
        Double time2 = g.getDirect()[g.getSetOfVertices().get(start)][g.getSetOfVertices().get(str2)];
        if(nbCorrespondances1 < nbCorrespondances2 || (nbCorrespondances1 == nbCorrespondances2 && time1 < time2)) return str1;
        else return str2;
    }

    private static Pair<String, String> startAndEnd(MatriceWGraph g, String start, String end){
        String start1 = start(g, start, end + "$1");
        String start2 = start(g, start, end + "$2");
        int nbCorrespondances1 = g.getIntermediate()[g.getSetOfVertices().get(start1)][g.getSetOfVertices().get(end + "$1")];
        Double time1 = g.getDirect()[g.getSetOfVertices().get(start1)][g.getSetOfVertices().get(end + "$1")];
        int nbCorrespondances2 = g.getIntermediate()[g.getSetOfVertices().get(start2)][g.getSetOfVertices().get(end + "$2")];
        Double time2 = g.getDirect()[g.getSetOfVertices().get(start2)][g.getSetOfVertices().get(end + "$2")];
        if(nbCorrespondances1 < nbCorrespondances2 || (nbCorrespondances1 == nbCorrespondances2 && time1 < time2)){
            return new Pair<String, String>(start1, end + "$1");
        }
        else return new Pair<String, String>(start2, end + "$2");
    }
}
