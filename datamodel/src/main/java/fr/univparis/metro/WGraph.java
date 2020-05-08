package fr.univparis.metro;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A weighted graph class
 */
public class WGraph<T> implements Cloneable{

  private HashMap<T, HashMap<T, Double>> wGraph;

  /**
  * Creates an empty Weighted Graph
  */
  public WGraph(){
    this.wGraph = new HashMap<T, HashMap<T, Double>>();
  }

  /**
  * Creates a clone of the graph.
  * Vertices will not be clone
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
  * Returns true if and only if the instance graph and the parameter graph represent the same network
  * @param g the WGraph to compare with the WGraph
  * @return True if and only if the instance graph and the parameter graph represent the same network
  */
  public boolean equals(WGraph<T> g){
    return(g.wGraph.equals(this.wGraph));
  }

  /**
  * Returns a set of all the vertices in the graph.
  * @return a set of all the vertices in the graph.
  */
  public Set<T> getVertices(){
    return this.wGraph.keySet();
  }
  /**
  * Returns the number of vertices of the WGraph
  * @return the number of vertices of the WGraph
  */
  public int nbVertex() {return wGraph.size();}

  /**
  * Returns the neighbors of a vertex contained in the graph.
  * @param vertex the vertex which we want the neighbors.
  * @return the set of all the vertices that can be reach by the parameter vertex
  */
  public Set<T> neighbors(T vertex){
    return wGraph.get(vertex).keySet();
  }


  /**
  * Returns the weight between two vertices
  * @param s the start of the edge
  * @param p the end of the edge
  * @return the weight between the vertex s and the vertex p.
  */
  public Double weight(T s, T p){
    if (wGraph.get(s).get(p) != null)
      return wGraph.get(s).get(p);
    else return Double.NaN;
  }


  /**
  * Adds a vertex to the graph.
  * @param v the vertex we want to add.
  * @return true if v has been added.
  */
  public boolean addVertex(T v){
    if(this.wGraph.get(v) == null){
      this.wGraph.put(v, new HashMap<T, Double> ());
      return true;
    }
    return false;
  }


  /**
  * Deletes a vertex of the graph and all the edges that are link to it
  * @param v the vertex we want to delete
  * @return true if v has been deleted, false if the vertex hasn't be found.
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
  * Adds an edge between two vertices
  * @param s the start of the edge.
  * @param p the end of the edge.
  * @param weight the weight of the edge.
  * @return true if the edge has correctly been added, else false (occurs when one of the vertex is vertex is not in this graph or that there is already an edge).
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
  * Removes an edge
  * @param s the start of the edge.
  * @param p the end of the edge.
  * @return true if the edge has correctly been removed (false occurs when the specified edge don't exist).
  */
  public boolean removeEdge(T s, T p){
    return wGraph.get(s).remove(p) != null;
  }

  /**
  * Adds double edge between the specified vertex and all the other vertices that satifies a condition
  * @param e one end of every added edge
  * @param weight the weight for the added edges
  * @param p an edge between the specified vertex and any other edges will be added if the other vertex satifiy this predicate
  * @return true if and only if the specified vertex is in the graph, else no edge will be add.
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
  * Return the number of Vertices that satifies a condition
  * @param p The condition for every vertex
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
  * Return a String containing the {@link java.lang.Object#toString} funtion of every vertex that evualuate true with predicate p
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
  * Inidicates the presence of the specified vertex in the graph
  * @param s the vertex we want know the presence in the graph
  * @return true if and only if the specified vertex is in the graph
  */
  public boolean containsVertex(T s){
    return wGraph.containsKey(s);
  }

  /**
  * Modifies the weight between two vertices
  * @param s the start of the edge
  * @param p the end of the edge
  * @param weight the new weight of the edge
  */
  public void setWeight(T s, T p, Double weight) {
    if (! wGraph.get(s).containsKey(p)) return;
    this.wGraph.get(s).put(p, weight);
  }

 /**
  * Adds and modifies the vertices and eges that are in the specified graph into the instance graph.
  * If a vertex of the specified graph is not in the instance graph it will be added.
  * Same for the edges.
  * If an edges between is in both graph the weight in the instance graph will be modify with the weight in the specified graph.
  * @param g the graph containing all the modification that we want to apply to the isntance graph
  */
  public void apply(WGraph<T> g) {
    for (T t : g.getVertices()) {
      if (! this.containsVertex(t)) this.addVertex(t);
      for (T n : g.neighbors(t)) {
        if (! this.containsVertex(n)) this.addVertex(n);
        this.wGraph.get(t).put(n, g.weight(t, n));
      }
    }
  }

 /**
  * Removes a vertex and replace it by two vertex wich will have the same edges.
  * @param old the vertex we want to split into two vertices
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
