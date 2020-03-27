package fr.univparis.metro;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public class GraphExporter {

    /**
     * Export a graph to the DOT format.
     * @param graph The graph to export to DOT.
     * @param path The path where to put the result.
     * @param T The type of vertex.
     */
    public static <T> void exportToDOT(WGraph<T> graph, File path, Function<T, String> desc) throws IOException {
	FileWriter fw = new FileWriter(path);

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
}
