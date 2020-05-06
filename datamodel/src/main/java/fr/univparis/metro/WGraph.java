package fr.univparis.metro;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Predicate;

public class WGraph<T> implements Cloneable{

  private HashMap<T, HashMap<T, Double>> wGraph;

  /**
  * Construct a WGraph with a corresponding hashmap for wGraph
  */
  public WGraph(){
    this.wGraph = new HashMap<T, HashMap<T, Double>>();
  }

  /**
  * Create a cloning WGraph by the WGraph
  * @return a clone of the WGraph
  */
  public WGraph<T> clone(){
    WGraph<T> c = new WGraph<T>();
    for(T t : this.getVertices()){
      HashMap<T, Double> h = new HashMap<T, Double>();
      for(T s : this.neighbors(t)){
        Double w = weight(t, s);
        h.put(s, w);
      }
      c.wGraph.put(t, h);
    }
    return c;
  }

  /**
  * Compare the WGraph g with the WGraph
  * @param g the WGraph to compare with the WGraph
  * @return true if there are equals else false
  */
  public boolean equals(WGraph<T> g){
    return(g.wGraph.equals(this.wGraph));
  }

  /**
  * Return the set of all the keys contained in the wGraph of WGraph
  * @return the set of all the keys contained in the HashMap wGraph.
  */
  public Set<T> getVertices(){
    return this.wGraph.keySet();
  }
  /**
  * Return the number of vertices of the WGraph
  * @return the number of vertices of the WGraph
  */
  public int nbVertex() {return wGraph.size();}

  /**
  * Give the value of a key T vertex in wGraph of the WGraph
  * @param vertex
  * @return a list that contains all the vertices we can reach from the vertex "vertex".
  */
  public Set<T> neighbors(T vertex){
    return wGraph.get(vertex).keySet();
  }


  /**
  * Return the weight between two vertices
  * @param s a vertex we want find the weight with the vertex p
  * @param p the other vertex
  * @return the weight between the vertex s and the vertex p.
  */
  public Double weight(T s, T p){
    if (wGraph.get(s).get(p) != null)
      return wGraph.get(s).get(p);
    else return Double.NaN;
  }


  /**
  * Add a vertex to the WGraph and return if it is good after that
  * @param v a vertex we add to the HashMap wGraph.
  * @return true if v has correctly been added, false otherwise.
  */
  public boolean addVertex(T v){
    if(this.wGraph.get(v) == null){
      this.wGraph.put(v, new HashMap<T, Double> ());
      return true;
    }
    return false;
  }


  /**
  * Delete a vertex v in the wGraph of the WGraph
  * @param v a vertex we delete from the HashMap wGraph.
  * @return true if v has been correctly deleted, false otherwise.
  */
  public boolean deleteVertex(T v){
    if(this.wGraph.containsKey(v)){
      for(T t : getVertices()){
        removeEdge(t, v);
      }
      this.wGraph.remove(v);
      return true;
    }
    return false;
  }


  /**
  * Create and add an edge between two vertices with a weight
  * @param s the start of the edge.
  * @param p the end of the edge.
  * @param weight the weight of the edge.
  * @return true if the edge has correctly been created and added to the HashMap wGraph.
  */
  public boolean addEdge(T s, T p, Double weight){
    if (s.equals(p)) return false;
    if(this.wGraph.containsKey(s) && this.wGraph.containsKey(p) && this.weight(s, p).equals(Double.NaN)) {
      this.wGraph.get(s).put(p, weight);
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
    return wGraph.get(s).remove(p) != null;
  }

  /**
  * Add double edge between e and every element of the graph that evaluate true with p
  * @param e the vertex wich we want to add Edge
  * @param weight the weight for the added Edge
  * @param p the Predicate we want that evaluate if we want to add an edge or not for every element of the graph
  */
  public boolean addDoubleEdge(T e, Double weight, Predicate<T> p) {
    if (!wGraph.containsKey(e)) return false;
    Set<T> set = wGraph.keySet();
    for (T t : set) {
      if (p.test(t)) {
        addEdge(e, t, weight);
        addEdge(t, e, weight);
      }
    }
    return true;
  }

  /**
  * Return the number of Vertex that evaluate true with predicate p
  * @param p The predicate to test for every vertex
  * @return The number of vertex that evaluate true with predicate p
  *
  */
  public int nbVertex(Predicate<T> p) {
    int res = 0;
    Set<T> set = wGraph.keySet();
    for (T t : set) {
      if (p.test(t)) res++;
    }
    return res;
  }

  /**
  * Return a String containing the toString funtion of every vertex that evualuate true with predicate p
  * @param p The predicate to test with every vertex
  * @return a String containing the toString funtion of every vertex that evualuate true with predicate p
  *
  */
  public String vertexToString(Predicate<T> p) {
    String res = "";
    for (T t : wGraph.keySet()) {
      if (p.test(t)) res += t.toString() + "\n";
    }
    return res;
  }

  /**
  * Verify if the WGraph contains a vertex s (in his keys)
  * @param s the vertex we want know the presence in the WGraph
  * @return true if it contains s else false
  */
  public boolean containsVertex(T s){
    return wGraph.containsKey(s);
  }

  /**
  * Modify the weight between two vertices
  * @param s      the first vertex
  * @param p      the second vertex
  * @param weight the weight that we want to put between these two vertices
  */
  public void setWeight(T s, T p, Double weight) {
    if (! wGraph.get(s).containsKey(p)) return;
    this.wGraph.get(s).put(p, weight);
  }

 /**
  * Add or modify the vertices that are in g into the instance object
  * Previous weight will be overwritten
  * @param g the graph containing all the modification that we want to apply to the isntance graph
  */
  public void apply( WGraph<T> g) {
    for (T t : g.getVertices()) {
      if (! this.containsVertex(t)) this.addVertex(t);
      for (T n : g.neighbors(t)) {
        if (! this.containsVertex(n)) this.addVertex(n);
        this.wGraph.get(t).put(n, g.weight(t, n));
      }
    }
  }
 /**
  * Remove a vertex and replace it by two vertex wich will have the same edges
  * @param old the vertex we want to split in two vertices
  * @param newVert1 one of the new vertex that replace old
  * @param newVert2 the other new vertex that replace old
  */
  public void splitVertex(T old, T newVert1, T newVert2) {
    if (! containsVertex(old)) return;
    addVertex(newVert1);
    addVertex(newVert2);
    for (T n : neighbors(old)) {
      addEdge(newVert1, n, weight(old, n));
      addEdge(newVert2, n, weight(old, n));
    }

    for (T t : getVertices()) {
      if (neighbors(t).contains(old)) {
        addEdge(t, newVert1, weight(t, old));
        addEdge(t, newVert2, weight(t, old));
      }
    }
    deleteVertex(old);
  }



}
