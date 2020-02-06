package fr.univparis.metro;

import java.util.*;
import java.io.*;

public class Parser {
  public static void loadFrom (File f) throws IOException {
    Scanner sc = new Scanner (f);
    sc.useDelimiter("\n");
    String s;
    while (sc.hasNext()) {
      s = sc.next();
      if (!(s.startsWith("Ligne") || s.startsWith("--"))) {
        Station.stations.add(new Station(s));
      }
    }
    sc.close();
  }
}
