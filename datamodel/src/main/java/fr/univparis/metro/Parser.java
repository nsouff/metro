package fr.univparis.metro;

import java.util.*;
import java.io.*;

public class Parser {

 /**
  * The default weight between station
  */
  public static final Double defaultWeight = 90.0;

 /**
  * The default weight for in station change
  */
  public static final Double defaultChangeStationWeight = 60.0;

 /**
  * Create a WGraph using a text file with stations on each line
  * In station change are weighted by {@value #defaudefaultChangeStationWeight}
  * Weight between station is {@value #defaultWeight}
  * @param f is the txt file wich contain the station
  * @return the WGraph that correspond to the file
  */
  public static WGraph<Station> loadFrom (InputStream is) throws IOException {
    Scanner sc = new Scanner (is);
    WGraph<Station> g = new WGraph<Station>();
    Station prec = null;
    String s;
    String line = "";
    HashSet<String> createdStation = new HashSet<String>();
    while (sc.hasNextLine()) {
      s = sc.nextLine();
      if (s.isEmpty()) continue;
      else if (s.startsWith("Ligne")) line = s.substring(6);
      else if (s.equals("--")) {
        line = "";
        prec = null;
      }
      else if (s.equals("{")) prec = cycle(g, sc, prec, line, createdStation);
      else if (s.equals("[")) prec = fork (g, sc, prec, line, createdStation);
      else if (s.equals("}")) throw new IllegalStateException("Can't close a cycle if there isn't an open one");
      else if (s.equals("]")) throw new IllegalStateException("Can't close a fork if there isn't an open one");
      else if (s.equals("/")) throw new IllegalStateException("There isn't an open cycle for character \"/\"");
      else prec = addNextStation(g, prec, s, line, true, true, createdStation);
    }

    return g;
  }

 /**
  * Used when there is a fork in the subway,
  * Create the two branch while there is no "]" in the {@link java.util.Scanner}
  * || is the separator for the two branch
  * @param g The graph that has been initialized and where the fork will be added
  * @param sc The Scanner with stations on every line
  * @param start The station before the fork, if it's null then the line is starting by a fork
  * @param line the line in wich the fork is
  * @param createdStation a Set to know wich Station has already been created (juste name of the station)
  * @return null if the fork ends the line or the station that linked the two branch
  */
  private static Station fork(WGraph<Station> g, Scanner sc, Station start, String line, HashSet<String> createdStation) {
    String s;
    Station endOfFirst = start;
    if (start != null){
      g.splitVertex(start, new Station(start.getName() + "$1", start.getLine()), new Station(start.getName() + "$2", start.getLine()));
      g.addEdge(new Station(start.getName() + "$1", start.getLine()), new Station(start.getName() + "$2", start.getLine()), defaultChangeStationWeight);
      g.addEdge(new Station(start.getName() + "$2", start.getLine()), new Station(start.getName() + "$1", start.getLine()), defaultChangeStationWeight);
    }

    Station prec = start;
    while(sc.hasNextLine()) {
      s = sc.nextLine();
      if (s.isEmpty()) continue;
      if (s.equals("]")) break;
      else if (s.equals("||")){
        endOfFirst = prec;
        prec = start;
      }
      else if (s.equals("[")) fork(g, sc, prec, line, createdStation);
      else if (s.equals("{")) cycle(g, sc, prec, line, createdStation);
      else{
        boolean b = prec == start && start != null;
        prec = addNextStation(g, prec, s, line, true, true, createdStation);
        if (b) {
          g.removeEdge(new Station(start.getName() + "$1", start.getLine()), prec);
          g.removeEdge(prec, new Station(start.getName() + "$2", start.getLine()));
        }
      }
    }
    if (start != null) return null;
    do {
      if (!sc.hasNextLine()) throw new IllegalStateException();
      s = sc.nextLine();
      if (s.isEmpty()) continue;
      addNextStation(g, endOfFirst, s + "$1", line, true, true, createdStation);
      addNextStation(g, prec, s + "$2", line, true, true, createdStation);

      break;
    } while (sc.hasNextLine());
    return new Station(s, line);
  }


 /**
  * Used when there is a cycle in the subway
  * Create the cylce that has only one direction
  * / is the character indicated in the {@link java.util.Scanner} to know where starts the other branche of the cycle
  * } is the character that indicates the end of the cycle
  * @param g The graph that has been initialized and where the fork will be added
  * @param sc The Scanner with stations on every line
  * @param start The station before the start of the cycle
  * @param line The line in wich the cycle is
  * @param createdStation a Set to know wich Station has already been created (juste name of the station)
  * @return the station that is right after the end of the cycle
  */
  private static Station cycle(WGraph<Station> g, Scanner sc, Station start, String line, HashSet<String> createdStation) {
    if (start == null) throw new IllegalStateException("Cycle can't start with a null station");
    g.splitVertex(start, new Station(start.getName() + "$1", start.getLine()), new Station(start.getName() + "$2", start.getLine()));
    g.addEdge(new Station(start.getName() + "$1", start.getLine()), new Station(start.getName() + "$2", start.getLine()), defaultChangeStationWeight);
    g.addEdge(new Station(start.getName() + "$2", start.getLine()), new Station(start.getName() + "$1", start.getLine()), defaultChangeStationWeight);
    String s;
    Station endOfFirst = start;
    Station prec = start;
    boolean comeback = false;
    while(sc.hasNextLine()) {
      s = sc.nextLine();
      if (s.isEmpty()) continue;
      if (s.equals("/")) {
        if (comeback) throw new IllegalStateException();
        comeback = true;
        endOfFirst = prec;
        prec = start;
      }
      else if (s.equals("{")) cycle(g, sc, prec, line, createdStation);
      else if (s.equals("[")) fork(g, sc, prec, line, createdStation);
      else if (s.equals("}")) break;
      else{
        boolean b = (prec == start);
        prec = addNextStation(g, prec, s, line, !comeback, comeback, createdStation);
        if (b) {
          g.removeEdge(new Station(start.getName() + "$1", start.getLine()), prec);
          g.removeEdge(prec , new Station(start.getName() + "$2", start.getLine()));
        }
      }
    }
    if (!comeback) throw new IllegalStateException();
    do {
      if (!sc.hasNextLine()) throw new IllegalStateException();
      s = sc.nextLine();
      if (s.isEmpty()) continue;
      addNextStation(g, endOfFirst, s + "$1", line, true, false, createdStation);
      addNextStation(g, prec, s + "$2", line, false, true, createdStation);
      break;
    } while (sc.hasNextLine());
    return new Station(s, line);

  }


 /**
  * Add edge beteen Station prec and the line named s in the line line
  * If a station with the same name has already been created, it also created change of line edges
  * @param g The graph in wich we want to add Edge
  * @param prec The station before the station we want to add
  * @param s The name of the station
  * @param line The line of the added Station
  * @param ahead True if we want to add an edge that start at prec and goes to the created station
  * @param behind True if we want to add an edge that start at the created station and goes to prec
  * @param createdStation The set of the station already created, to know if we should add in station Edge
  * @return The new Station(s, line)
  */
  private static Station addNextStation(WGraph<Station> g, Station prec, String s, String line, boolean ahead, boolean behind, HashSet<String> createdStation) {
    Station act = new Station(s, line);
    g.addVertex(act);
    if (ahead && prec != null) {
      if (g.containsVertex(prec)) g.addEdge(prec, act, defaultWeight);
      else {
        g.addEdge(new Station(prec.getName() + "$1", prec.getLine()), act, defaultWeight);
        g.addEdge(new Station(prec.getName() + "$2", prec.getLine()), act, defaultWeight);
      }

    }
    if (behind && prec != null) {
      if (g.containsVertex(prec)) g.addEdge(act, prec, defaultWeight);
      else {
        g.addEdge(act, new Station(prec.getName() + "$1", prec.getLine()), defaultWeight);
        g.addEdge(act, new Station(prec.getName() + "$2", prec.getLine()), defaultWeight);
      }
    }

    addMetaStationAndChanging(g, act, createdStation, s);
    return act;
  }

  private static void addMetaStationAndChanging(WGraph<Station> g, Station act, HashSet<String> createdStation, String s) {
    boolean isASplit = s.contains("$");
    String name = (isASplit) ? s.substring(0, s.length() - 2) : s;
    if (! createdStation.contains(name)) {
      g.addVertex(new Station(name, "Meta Station Start"));
      g.addVertex(new Station(name, "Meta Station End"));
      createdStation.add(name);
    }
    else {
      g.addDoubleEdge(act, defaultChangeStationWeight, (t -> (t.getName().equals(name) || t.getName().startsWith(name + "$")) && !t.getLine().startsWith("Meta Station")));
    }

    g.addEdge(new Station(name, "Meta Station Start"), act, 0.0);
    g.addEdge(act, new Station(name, "Meta Station End"), 0.0);
  }

}
