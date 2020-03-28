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
        int numVertex = 0;
        this.setOfVertices = new HashMap<String, Integer>();
        for(Station s : g.getWGraph().keySet()){
            String vertexName = s.getName();
            if(!this.setOfVertices.containsKey(vertexName)){
                this.setOfVertices.put(vertexName, numVertex);
                numVertex++;
            }
        }
        int n = this.setOfVertices.size();
        this.direct = new Double[n][n];
        this.via = new Station[n][n];
        this.intermediate = new Integer[n][n];
        for(Map.Entry<Station, List<Pair<Station, Double>>> edge : g.getWGraph().entrySet()){
            Station vertex = edge.getKey();
            String vertexName = vertex.getName();
            this.direct[this.setOfVertices.get(vertexName)][this.setOfVertices.get(vertexName)] = 0.0;
            this.via[this.setOfVertices.get(vertexName)][this.setOfVertices.get(vertexName)] = vertex;
            this.intermediate[this.setOfVertices.get(vertexName)][this.setOfVertices.get(vertexName)] = 0;
            List<Pair<Station, Double>> verticesN = edge.getValue();
            for(Pair<Station, Double> p : verticesN){
                this.direct[this.setOfVertices.get(vertexName)][this.setOfVertices.get(p.getObj().getName())] = p.getValue();
                this.via[this.setOfVertices.get(vertexName)][this.setOfVertices.get(p.getObj().getName())] = vertex;
                this.intermediate[this.setOfVertices.get(vertexName)][this.setOfVertices.get(p.getObj().getName())] = 1;
            }
            for(Station s : g.getWGraph().keySet()){
                if(vertex.getLine().equals(s.getLine()) && !vertexName.equals(s.getName())){
                    this.via[this.setOfVertices.get(vertexName)][this.setOfVertices.get(s.getName())] = vertex;
                    this.intermediate[this.setOfVertices.get(vertexName)][this.setOfVertices.get(s.getName())] = 1;
                }
            }
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(this.direct[i][j] == null) this.direct[i][j] = Double.POSITIVE_INFINITY;
                if(this.intermediate[i][j] == null) this.intermediate[i][j] = Integer.MAX_VALUE;
            }
        }
        Double[][] t = initializeDirectLine(this.direct);
        for(Station s1 : g.getWGraph().keySet()){
            for(Station s2 : g.getWGraph().keySet()){
                if(s1.getLine().equals(s2.getLine()) && !s1.getName().equals(s2.getName())){
                    this.direct[this.setOfVertices.get(s1.getName())][this.setOfVertices.get(s2.getName())] = t[this.setOfVertices.get(s1.getName())][this.setOfVertices.get(s2.getName())];
                }
            }
        }
    }

    private static Double[][] initializeDirectLine(Double[][] d){
        Double[][] copy = new Double[d.length][d.length];
        for(int i = 0; i < copy.length; i++){
            for(int j = 0; j < copy.length; j++){
                copy[i][j] = d[i][j];
            }
        }
        copy = LimitedConnectionSearch.floyd(copy);
        return copy;
    }
}