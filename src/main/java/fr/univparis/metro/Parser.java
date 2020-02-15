package fr.univparis.metro;

import java.util.*;
import java.io.*;

public class Parser {

 /**
  * the default weight for the weight graph in {@link loadFrom}
  */
  private static final Double defaultWeight = 90.0;

  private static final Double defaultChangeStationWeight = 60.0;

 /**
  * Create a WGraph using a text file with stations on each line
  * @param f is the txt file wich contain the station
  * @return the WGraph with every weight = {@link defaultWeight}
  */
  public static WGraph<Station> loadFrom (File f) throws IOException {
    Scanner sc = new Scanner (f);
    WGraph<Station> g = new WGraph<Station>();
    sc.useDelimiter("\n");
    Station prec = null;
    String s;
    String line = "";
    HashSet<String> createdStation = new HashSet<String>();
    while (sc.hasNext()) {
      s = sc.next();
      if (s.isEmpty()) continue;
      else if (s.startsWith("Ligne")) line = s;
      else if (s.equals("--")) {
        line = "";
        prec = null;
      }
      else if (s.equals("{")) prec = cycle(g, sc, prec, line, createdStation);
      else if (s.equals("["))        fork (g, sc, prec, line, createdStation);
      else if (s.equals("}")) throw new IllegalStateException("Can't close a cycle if there isn't an open one");
      else if (s.equals("]")) throw new IllegalStateException("Can't close a fork if there isn't an open one");
      else if (s.equals("/")) throw new IllegalStateException("There isn't an open cycle for character \"/\"");
      else prec = addNextStation(g, prec, s, line, true, true, createdStation);
    }

    return g;
  }

  private static void fork(WGraph<Station> g, Scanner sc, Station fork, String line, HashSet<String> createdStation) {
    if (fork == null) throw new IllegalStateException("Fork can't start with null station");
    String s;
    Station prec = fork;
    while(sc.hasNext()) {
      s = sc.next();
      if (s.equals("]")) return;
      else if (s.equals("||")) prec = addNextStation(g, fork, s, line, true, true, createdStation);
      else if (s.equals("[")) fork(g, sc, prec, line, createdStation);
      else if (s.equals("{")) cycle(g, sc, prec, line, createdStation);
      else prec = addNextStation(g, prec, s, line, true, true, createdStation);
    }
  }

  private static Station cycle(WGraph<Station> g, Scanner sc, Station start, String line, HashSet<String> createdStation) {
    if (start == null) throw new IllegalStateException("Cycle can't start with a null station");
    String s;
    Station endOfFirst = start;
    Station prec = start;
    boolean comeback = false;
    while(sc.hasNext()) {
      s = sc.next();
      if (s.equals("/")) {
        if (comeback) throw new IllegalStateException();
        comeback = true;
        endOfFirst = prec;
        prec = start;
      }
      else if (s.equals("{")) cycle(g, sc, prec, line, createdStation);
      else if (s.equals("[")) fork(g, sc, prec, line, createdStation);
      else if (s.equals("}")) break;
      else addNextStation(g, prec, s, line, !comeback, comeback, createdStation);
    }
    if (!comeback || !sc.hasNext()) throw new IllegalStateException();
    s = sc.next();
    addNextStation(g, endOfFirst, s, line, true, false, createdStation);
    addNextStation(g, prec, s, line, false, true, createdStation);
    return new Station(s, line);
  }


 /**
  * Add a {@link Station} named s to the {@link Wgraph} g if s is a station name and if it's not in g yet
  * if s is not, the function just return prec without doing anything
  * if s is a station name, it will add an Edge between prec and the station
  * @param g the {@link WGraph} into wich we want to add the station
  * @param prec the station before the station name s, the two vertex will be linked by the weight {@link defaultWeight}
  * @param s the name of the station
  * @return the Station we Station we just created or prec if we didn't
  */
  private static Station addNextStation(WGraph<Station> g, Station prec, String s, String line, boolean ahead, boolean behind, HashSet<String> createdStation) {
    Station act = new Station(s, line);
    g.addVertex(act);
    if (ahead && prec != null)  g.addEdge(prec, act, defaultWeight);
    if (behind && prec != null) g.addEdge(act, prec, defaultWeight);
    if (! createdStation.contains(s))
      createdStation.add(s);
    else
      g.addDoubleEdge(act, defaultChangeStationWeight, (t -> act.sameName(t)));
    return act;
  }

}
