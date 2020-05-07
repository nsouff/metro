package fr.univparis.metro;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public class GraphExporter {

  /**
  * Export a graph to the DOT format.
  * @param graph The graph to export to DOT.
  * @param output The file in which we write the result.
  * @param desc The naming function for vertex.
  * @param <T> The type of vertex.
  * @throws IOException if the file is not correctly opened
  */
  public static <T> void exportToDOT(WGraph<T> graph, File output, Function<T, String> desc) throws IOException {
    FileWriter fw = new FileWriter(output);

    fw.write("digraph");
    fw.write(" {\n");

    for(T vertex : graph.getVertices() ) {
      for( T n : graph.neighbors(vertex) ) {
        fw.write("\"" + desc.apply(vertex) + "\" -> \"" + desc.apply(n) + "\";\n");
      }
    }

    fw.write("}");
    fw.flush();
    fw.close();
  }

  public static void main(String[] args) throws IOException {
    if (args.length < 1) {
      System.out.println("One city as a parameter is needed");
      return;
    }
    Configuration.loadFrom(GraphExporter.class.getResourceAsStream("/cities.json"));
    WGraph<Station> g = Parser.loadFrom(GraphExporter.class.getResourceAsStream("/" + Configuration.getFileName(args[0])));
    File out = new File("../" + args[0].toLowerCase() + ".dot");
    exportToDOT(g, out, Station::toString);


  }

}
