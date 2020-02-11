package fr.univparis.metro;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class WGraph<T>{

    private HashMap<T, List<Edge>> wGraph;

    public HashMap<T, List<Edge>> getWGraph(){
        return this.wGraph;
    }

    public WGraph(){
        this.wGraph = new HashMap<T, List<Edge>>();
    }

    class Edge{

        private T vertex;

        private Double weight;

        public T getVertex(){
            return this.vertex;
        }

        public Double getWeight(){
            return this.weight;
        }

        public Edge(T vertex, Double weight){
            this.vertex = vertex;
            this.weight = weight;
        }
    }

    public static WGraph<String> createGraph(){
        WGraph<String> g = new WGraph<String>();
        return g;
    }

    public List<T> neighbors(T vertex){
        ArrayList<T> ret = new ArrayList<T>();
        List<Edge> edges = this.wGraph.get(vertex);
        for(int i=0; i<edges.size(); i++){
            ret.add(edges.get(i).getVertex());
        }
        return ret;
    }

    public Double weight(T s, T p){
        List<Edge> edges = this.wGraph.get(s);
        for(int i=0; i<edges.size(); i++){
            Edge e = edges.get(i);
            if(p.equals(e.getVertex())) return e.getWeight();
        }
        return Double.NaN;
    }

    public boolean addVertex(T v){
        if(! this.wGraph.containsKey(v)){
            this.wGraph.put(v, new ArrayList<Edge>());
            return true;
        }
        return false;
    }

    public boolean deleteVertex(T v){
        Edge rm = null;
        if(this.wGraph.get(v)!=null){
            for(Map.Entry<T, List<Edge>> c : this.wGraph.entrySet()){
                for(Edge e : c.getValue()){
                    if(e.getVertex().equals(v)){
                        rm = e;
                        break;
                    }
                }
                if(rm!=null) c.getValue().remove(rm);
            }
            this.wGraph.remove(v);
            return true;
        }
        return false;
    }

    public boolean addEdge(T s, T p, Double weight){
        if(this.wGraph.get(s)!=null && this.wGraph.get(p)!=null){
            this.wGraph.get(s).add(new Edge(p, weight));
            return true;
        }
        return false;
    }

    public boolean removeEdge(T s, T p){
        Edge rm = null;
        if(this.wGraph.get(s)!=null && this.wGraph.get(p)!=null){
            List<Edge> l_s = this.wGraph.get(s);
            for(Edge e : l_s){
                if(e.getVertex()==p){
                    rm = e;
                    break;
                }
            }
            l_s.remove(rm);
            return true;
        }
        return false;
    }

    public int nbVertex() {return wGraph.size();}

}
