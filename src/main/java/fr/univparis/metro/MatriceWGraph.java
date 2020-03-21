package fr.univparis.metro;

import java.util.*;

public class MatriceWGraph{

    private HashMap<String, Integer> setOfVertices;
    //the initialization of the matrices Direct, Via and Intermediate we need for the limited connexion search algorithm.
    private Double[][] direct;
    private Station[][] via;
    private Integer[][] intermediate;

    public HashMap<String, Integer> getSetOfVertices(){
        return this.setOfVertices;
    }

    public Double[][] getDirect(){
        return this.direct;
    }

    public Station[][] getVia(){
        return this.via;
    }

    public Integer[][] getIntermediate(){
        return this.intermediate;
    }

    public MatriceWGraph(WGraph<Station> g){
        int n = g.getWGraph().size();
        int numVertex = 0;
        this.setOfVertices = new HashMap<String, Integer>();
        this.direct = new Double[n][n];
        this.via = new Station[n][n];
        for(Map.Entry<Station, List<Pair<Station, Double>>> edge : g.getWGraph().entrySet()){
            String vertexName = edge.getKey().getName();
            if(!this.setOfVertices.containsKey(vertexName)){
                this.setOfVertices.put(vertexName, numVertex);
                numVertex++;
            }
        }
        for(Map.Entry<Station, List<Pair<Station, Double>>> edge : g.getWGraph().entrySet()){
            Station vertex = edge.getKey();
            String vertexName = vertex.getName();
            List<Pair<Station, Double>> verticesN = edge.getValue();
            for(Pair<Station, Double> p : verticesN){
                this.direct[this.setOfVertices.get(vertexName)][this.setOfVertices.get(p.getObj().getName())] = p.getValue();
                this.via[this.setOfVertices.get(vertexName)][this.setOfVertices.get(p.getObj().getName())] = vertex;
                this.intermediate[this.setOfVertices.get(vertexName)][this.setOfVertices.get(p.getObj().getName())] = 1;
            }
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(this.direct[i][j] == null) this.direct[i][j] = Double.POSITIVE_INFINITY;
                if(this.intermediate[i][j] == null) this.intermediate[i][j] = Integer.MAX_VALUE;
            }
        }
    }
}