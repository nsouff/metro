package fr.univparis.metro;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;


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

    /**
     * @return the set of all the keys contained in the HashMap wGraph.
     */
    public Set<T> getVertices(){
        return this.getWGraph().keySet();
    }

    public int nbVertex() {return wGraph.size();}
    
    /**
     * @param vertex 
     * @return a list that contains all the vertices we can reach from the vertex "vertex".
     */
    public List<T> neighbors(T vertex){
        ArrayList<T> ret = new ArrayList<T>();
        List<Edge> edges = this.wGraph.get(vertex);
        for(int i=0; i<edges.size(); i++){
            ret.add(edges.get(i).getVertex());
        }
        return ret;
    }


    /**
     * @param s
     * @param p
     * @return the weight between the vertex s and the vertex p.
     */
    public Double weight(T s, T p){
        List<Edge> edges = this.wGraph.get(s);
        for(int i=0; i<edges.size(); i++){
            Edge e = edges.get(i);
            if(p.equals(e.getVertex())) return e.getWeight();
        }
        return Double.NaN;
    }


    /**
     * @param v a vertex we add to the HashMap wGraph.
     * @return true if v has correctly been added, false otherwise.
     */
    public boolean addVertex(T v){
        if(this.wGraph.get(v)==null){
            this.wGraph.put(v, new ArrayList<Edge>());
            return true;
        }
        return false;
    }


    /**
     * @param v a vertex we delete from the HashMap wGraph.
     * @return true if v has been correctly deleted, false otherwise.
     */
    public boolean deleteVertex(T v){
        Edge rm = null;
        if(this.wGraph.containsKey(v)){
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


    /**
     * @param s the start of the edge.
     * @param p the end of the edge.
     * @param weight the weight of the edge.
     * @return true if the edge has correctly been created and added to the HashMap wGraph.
     */
    public boolean addEdge(T s, T p, Double weight){
        if(this.wGraph.containsKey(s) && this.wGraph.containsKey(p)){
            this.wGraph.get(s).add(new Edge(p, weight));
            return true;
        }
        return false;
    }


    /**
     * Remove the vertex that has the vertices s and p.
     * @param s one of the vertex of the edge we want to remove.
     * @param p one of the vertex of the edge we want to remove.
     * @return true if the edge has correctly been removed.
     */
    public boolean removeEdge(T s, T p){
        Edge rm = null;
        if(this.wGraph.containsKey(s) && this.wGraph.containsKey(p)){
            List<Edge> l_s = this.wGraph.get(s);
            for(Edge e : l_s){
                if(e.getVertex().equals(p)){
                    rm = e;
                    break;
                } 
            }
            l_s.remove(rm);
            return true;
        }
        return false;
    }



}

