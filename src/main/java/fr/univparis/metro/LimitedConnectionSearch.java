package fr.univparis.metro;

import java.util.Set;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

public class LimitedConnectionSearch{

    public static WGraph createGraphLine(WGraph g, String line){
        WGraph ret = new WGraph();
        ArrayList<SimpleEntry<Station, ArrayList<Pair<Station, Double>>>> tmp = new ArrayList<SimpleEntry<Station, ArrayList<Pair<Station, Double>>>>();
        Set<SimpleEntry<Station, ArrayList<Pair<Station, Double>>>> set = g.getWGraph().entrySet();
        for(SimpleEntry<Station, ArrayList<Pair<Station, Double>>> m : set){
            if(m.getKey().getName().equals(line)) tmp.add(m);
        }
        return ret;
    }

    public static Double[][] directMatrix(WGraph g){
        Double[][] ret = new Double[g.nbVertex()][g.nbVertex()];
        return ret;
    }
}