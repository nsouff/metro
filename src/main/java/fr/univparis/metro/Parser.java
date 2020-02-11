package fr.univparis.metro;

import java.util.*;
import java.io.*;

public class Parser {

 /**
  * String we don't use yet in the parser
  */
  private final static String[] spe = {"[", "]", "/", "||", "Ligne", "--"};

 /**
  * the default weight for the weight graph in {@link loadFrom}
  */
  private final static Double defaultWeight = 90.0;

 /**
  * Create a WGraph using a text file with stations on each line
  * @param f is the txt file wich contain the station
  * @return the WGraph with every weight = {@link defaultWeight}
  */
  public static WGraph<Station> loadFrom (File f) throws IOException {
    Scanner sc = new Scanner (f);
    WGraph<Station> g = new WGraph<Station>();
    sc.useDelimiter("\n");
    Station st = null;
    while (sc.hasNext())
      st = addNextStation(g, st, sc.next());
    sc.close();
    return g;
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
  private static Station addNextStation(WGraph<Station> g, Station prec, String s) {
    if (isStation(s)) {
      Station act = new Station(s);
      g.addVertex(act);
      g.addEdge(act, prec, defaultWeight);
      g.addEdge(prec, act, defaultWeight);
      return act;
    }
    return prec;
  }

 /**
  * Returns true if s is a station name
  * @param s the String we want to know if it's station name
  * @return true if s isn't empty and doesn't start with one of the element of {@link spe}
  */
  private static boolean isStation(String s) {
    boolean res = true;
    for (String sp : spe) res &= !s.startsWith(sp);
    return (res && !s.isEmpty());
  }
}
