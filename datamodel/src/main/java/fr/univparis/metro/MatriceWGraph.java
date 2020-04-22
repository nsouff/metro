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

    //To build a graph of a line
    private MatriceWGraph(WGraph<Station> g){
        int numVertex = 0;
        this.setOfVertices = new HashMap<String, Integer>();
        for(Station s : g.getVertices()){
            String vertexName = s.getName();
            String vertexLine = s.getLine();
            if(!this.setOfVertices.containsKey(vertexName) && vertexLine != "Meta Station Start" && vertexLine != "Meta Station End"){
                this.setOfVertices.put(vertexName, numVertex);
                numVertex++;
            }
        }
        int n = this.setOfVertices.size();
        this.direct = new Double[n][n];
        this.via = new Station[n][n];
        this.intermediate = new Integer[n][n];
        for(Station vertex : g.getVertices()){
            String vertexName = vertex.getName();
            String vertexLine = vertex.getLine();
            if(vertexLine != "Meta Station Start" && vertexLine != "Meta Station End"){
                this.direct[this.setOfVertices.get(vertexName)][this.setOfVertices.get(vertexName)] = 0.0;
                this.via[this.setOfVertices.get(vertexName)][this.setOfVertices.get(vertexName)] = vertex;
                this.intermediate[this.setOfVertices.get(vertexName)][this.setOfVertices.get(vertexName)] = 0;
                Set<Station> verticesN = g.neighbors(vertex);
                for(Station p : verticesN){
                    if(p.getLine() != "Meta Station Start" && p.getLine() != "Meta Station End"){
                        this.direct[this.setOfVertices.get(vertexName)][this.setOfVertices.get(p.getName())] = g.weight(vertex, p);
                        this.via[this.setOfVertices.get(vertexName)][this.setOfVertices.get(p.getName())] = vertex;
                        this.intermediate[this.setOfVertices.get(vertexName)][this.setOfVertices.get(p.getName())] = 1;
                    }
                }
            }
        }
        //all the coefficients that have not been initialize are initialized to infinity
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(this.direct[i][j] == null) this.direct[i][j] = Double.POSITIVE_INFINITY;
                if(this.intermediate[i][j] == null) this.intermediate[i][j] = Integer.MAX_VALUE;
            }
        }
        Double[][] t = initializeDirectLine(this.direct);
        for(Station s1 : g.getVertices()){
            if(s1.getLine().equals("Meta Station Start") || s1.getLine().equals("Meta Station End")) continue;
            for(Station s2 : g.getVertices()){
                if(s2.getLine().equals("Meta Station Start") || s2.getLine().equals("Meta Station End")) continue;
                if(s1.getLine().equals(s2.getLine()) && !s1.getName().equals(s2.getName())){
                    this.direct[this.setOfVertices.get(s1.getName())][this.setOfVertices.get(s2.getName())] = t[this.setOfVertices.get(s1.getName())][this.setOfVertices.get(s2.getName())];
                    this.via[this.setOfVertices.get(s1.getName())][this.setOfVertices.get(s2.getName())] = s1;
                    this.intermediate[this.setOfVertices.get(s1.getName())][this.setOfVertices.get(s2.getName())] = 1;
                }
            }
        }
    }

    /**
     * To build the whole graph.
     * @param g is the graph under the from of a hashMap from which we build the matrice graph.
     * @param m contains all the sub graphs representing each lines of the graph g.
     */
    public MatriceWGraph(WGraph<Station> g, HashMap<String, MatriceWGraph> m){
        int numVertex = 0;
        this.setOfVertices = new HashMap<String, Integer>();
        for(Station s : g.getVertices()){
            String vertexName = s.getName();
            String vertexLine = s.getLine();
            if(!this.setOfVertices.containsKey(vertexName) && vertexLine != "Meta Station Start" && vertexLine != "Meta Station End"){
                this.setOfVertices.put(vertexName, numVertex);
                numVertex++;
            }
        }
        int n = this.setOfVertices.size();
        this.direct = new Double[n][n];
        this.via = new Station[n][n];
        this.intermediate = new Integer[n][n];
        for(Station vertex : g.getVertices()){
            String vertexName = vertex.getName();
            String vertexLine = vertex.getLine();
            if(vertexLine != "Meta Station Start" && vertexLine != "Meta Station End"){
                this.direct[this.setOfVertices.get(vertexName)][this.setOfVertices.get(vertexName)] = 0.0;
                this.via[this.setOfVertices.get(vertexName)][this.setOfVertices.get(vertexName)] = vertex;
                this.intermediate[this.setOfVertices.get(vertexName)][this.setOfVertices.get(vertexName)] = 0;
                Set<Station> verticesN = g.neighbors(vertex);
                for(Station p : verticesN){
                    if(p.getLine() != "Meta Station Start" && p.getLine() != "Meta Station End"){
                        this.direct[this.setOfVertices.get(vertexName)][this.setOfVertices.get(p.getName())] = g.weight(vertex, p);
                        this.via[this.setOfVertices.get(vertexName)][this.setOfVertices.get(p.getName())] = vertex;
                        this.intermediate[this.setOfVertices.get(vertexName)][this.setOfVertices.get(p.getName())] = 1;
                    }
                }
            }
        }
        //all the coefficients that have not been initialize are initialized to infinity
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(this.direct[i][j] == null) this.direct[i][j] = Double.POSITIVE_INFINITY;
                if(this.intermediate[i][j] == null) this.intermediate[i][j] = Integer.MAX_VALUE;
            }
        }
        for(Station s1 : g.getVertices()){
            if(s1.getLine().equals("Meta Station Start") || s1.getLine().equals("Meta Station End")) continue;
            for(Station s2 : g.getVertices()){
                if(s2.getLine().equals("Meta Station Start") || s2.getLine().equals("Meta Station End")) continue;
                if(s1.getLine().equals(s2.getLine()) && !s1.getName().equals(s2.getName())){
                    MatriceWGraph gm = m.get(s1.getLine());
                    int x1 = this.setOfVertices.get(s1.getName());
                    int y1 = this.setOfVertices.get(s2.getName());
                    int x2 = gm.setOfVertices.get(s1.getName());
                    int y2 = gm.setOfVertices.get(s2.getName());
                    this.direct[x1][y1] = gm.direct[x2][y2];
                    this.via[this.setOfVertices.get(s1.getName())][this.setOfVertices.get(s2.getName())] = s1;
                    this.intermediate[this.setOfVertices.get(s1.getName())][this.setOfVertices.get(s2.getName())] = 1;
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

    private static HashMap<String, WGraph<Station>> graphsLine(WGraph<Station> g){
        HashMap<String, WGraph<Station>> ret = new HashMap<String, WGraph<Station>>();
        for(Station s : g.getVertices()){
            if(s.getLine().equals("Meta Station Start") || s.getLine().equals("Meta Station End")) continue;
            if(!ret.containsKey(s.getLine())) ret.put(s.getLine(), new WGraph<Station>());
            WGraph<Station> m = ret.get(s.getLine());
            m.addVertex(s);
            Set<Station> l = g.neighbors(s);
            for(Station p :l){
                m.addVertex(p);
                m.addEdge(s, p, g.weight(s, p));
            }
        }
        return ret;
    }

    public static HashMap<String, MatriceWGraph> initializeAllLineGraphs(WGraph<Station> g){
        HashMap<String, WGraph<Station>> h = graphsLine(g);
        HashMap<String, MatriceWGraph> ret = new HashMap<String, MatriceWGraph>();
        for(String line : h.keySet()){
            MatriceWGraph m = new MatriceWGraph(h.get(line));
            ret.put(line, m);
        }
        return ret;
    }
}
