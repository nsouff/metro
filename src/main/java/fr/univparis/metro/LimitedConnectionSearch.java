package fr.univparis.metro;

import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class LimitedConnectionSearch {

    public static WGraph<Station> createGraphLine(WGraph<Station> g, String line) {
        WGraph<Station> ret = new WGraph<Station>();
        ArrayList<Entry<Station, List<Pair<Station, Double>>>> tmp = new ArrayList<Entry<Station, List<Pair<Station, Double>>>>();
        Set<Entry<Station, List<Pair<Station, Double>>>> set = g.getWGraph().entrySet();
        for(Entry<Station, List<Pair<Station, Double>>> m : set){
            if(m.getKey().getLine().equals(line)) tmp.add(m);
        }
        return ret;
    }

    public static Double[][] directMatrix(WGraph<Station> g){
        Double[][] ret = new Double[g.nbVertex()][g.nbVertex()];
        return ret;
    }
}