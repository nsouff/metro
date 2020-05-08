package fr.univparis.metro;

public class PathVueTest {
  /* WARNING: The test takes to much time but gives you an idea of how fast is the algo */
  // @Test
  public void multiplePathEndsTest() {
    int count = 0;
    WGraph<Station> g = Parser.loadFrom(this.getClass().getResourceAsStream("/paris.txt"));
    for (Station start : g.getVertices()) {
      if (! start.getLine().equals("Meta Station Start")) continue;
      System.out.println(count++);
      int c = 0;
      for (Station to : g.getVertices()) {
        if (! to.getLine().equals("Meta Station End")) continue;
        System.out.println("--" + c++);
        try {
          PathVue.multiplePath(g, start, to);
        } catch(Exception e) {
          e.printStackTrace();
          System.out.println("start: " + start.getName() + " " + start.getLine());
          System.out.println("to: " + to.getName() + " " + to.getLine());
        }
      }
    }
  }
}
