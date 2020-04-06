package fr.univparis.metro;

import java.io.IOException;
import java.io.File;
import java.util.HashMap;

public class Trafics {

  static HashMap<String, WGraph<Station>> trafics;

  public static void initTrafics() {
    trafics = new HashMap<String, WGraph<Station>> ();
    for (String city : Configuration.getCitiesName()) {
      try {
        trafics.put(city, Parser.loadFrom(new File(Configuration.getFileName(city))));
      } catch(IOException e) {
        e.printStackTrace();
      }
    }
  }


   /**
    * Modify the graph g so that we can't take a line
    * @param g is the graph we want to modify
    * @param line is the line we want to shutdown
    * @return a WGraph that we can use to revert this perturbation
    */
    public static WGraph<Station> shutdownLine(WGraph<Station> g, String line) {
      WGraph<Station> revert = new WGraph<Station>();
      for (Station s : g.getVertices()) {
        if (! s.getLine().equals(line)) continue;
        if (! revert.containsVertex(s)) revert.addVertex(s);
        for (Station n : g.neighbors(s)) {
          if (! n.getLine().equals(line)) continue;
            if (! revert.containsVertex(n)) revert.addVertex(n);
            revert.addEdge(s, n, g.weight(s, n));
            g.setWeight(s, n, Double.POSITIVE_INFINITY);
        }
      }
      return revert;
    }

}
