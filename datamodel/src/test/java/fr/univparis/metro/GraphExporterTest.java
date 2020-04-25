package fr.univparis.metro;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.security.MessageDigest;

public class GraphExporterTest {

    private static byte[] createChecksum(File file) throws Exception {
	InputStream fis =  new FileInputStream(file);

	byte[] buffer = new byte[1024];
	MessageDigest complete = MessageDigest.getInstance("MD5");
	int numRead;

	do {
	    numRead = fis.read(buffer);
	    if (numRead > 0) {
		complete.update(buffer, 0, numRead);
	    }
	} while (numRead != -1);

	fis.close();
	return complete.digest();
    }

    private static String getMD5Checksum(File file) throws Exception {
	byte[] b = createChecksum(file);
	String result = "";

	// convert byte array to string
	for (int i=0; i < b.length; i++) {
	    result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	}
	return result;
    }

    @Test
    public void exportToDOTTest() throws IOException {
	WGraph<Station> graph = new WGraph<Station>();

	graph = Parser.loadFrom(this.getClass().getResourceAsStream("/liste.txt"));

	try {
	    File out = new File("./graph.dot");
	    GraphExporter.exportToDOT(graph, out, Station::toString);

	    String actual = getMD5Checksum(out);
	    assertEquals("44076045993dfc6a437003298a550502", actual);

	    out.delete();
	} catch(Exception e) {
	    e.printStackTrace();
	    fail();
	}
    }
}
