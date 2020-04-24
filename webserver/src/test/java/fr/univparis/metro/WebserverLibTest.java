package fr.univparis.metro;
import java.io.IOException;
import org.junit.*;

public class WebserverLibTest {
  /* WARNING: The test takes to much time but gives you an idea of how fast is the algo */
  // @Test
  public void multiplePathEndsTest() throws IOException{
    int count = 0;
    WGraph<Station> g = Parser.loadFrom(this.getClass().getResourceAsStream("/liste.txt"));
    for (Station start : g.getVertices()) {
      if (! start.getLine().equals("Meta Station Start")) continue;
      System.out.println(count++);
      int c = 0;
      for (Station to : g.getVertices()) {
        if (! to.getLine().equals("Meta Station End")) continue;
        System.out.println("--" + c++);
        try {
          WebserverLib.multiplePath(g, start, to);
        } catch(Exception e) {
          e.printStackTrace();
          System.out.println("start: " + start.getName() + " " + start.getLine());
          System.out.println("to: " + to.getName() + " " + to.getLine());
        }
      }
    }
  }
}
