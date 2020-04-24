package fr.univparis.metro;

import java.util.*;
import java.io.*;

public class WebserverLib {
  public static String toOption() {
    String res = "";
    boolean first = true;
    for (String s : Configuration.getCitiesName()) {
      res += "<option " + ((first)? "selected": "") + " value = \"" + s + "\">" + s + "</option>\n";
    }
    return res;
  }

  private static String path(HashMap<Station, Station> prev, Station to, ArrayList<Pair<Station, Station>> changingStation) {
    changingStation.clear();
    LinkedList<Station> path = new LinkedList<Station>();
    Station current  = to;
    while(! prev.get(current).getLine().equals("Meta Station Start")) {
      current = prev.get(current);
      path.add(current);
    }
    Collections.reverse(path);
    String from = path.getFirst().getName();
    String line = path.getFirst().getLine();
    String res = "Departure : " + from + "<br><br>"+"line " + line + " : " + from + " -> ";
    Station prec = null;
    for (Station st : path) {
      if (!st.getLine().equals(line)) {
        changingStation.add(new Pair<Station, Station>(prec, st));
        res += st.getName() + "<br>" + "line " + st.getLine() + " : " + st.getName() + " -> ";
        line = st.getLine();
      }
      prec = st;
    }
    res += to.getName() + "<br><br>Arrival: " + to.getName();
    return res;
  }

  public static String path(ArrayList<Pair<String, String>> l, String from, String to){
    String ret = "Departure " + from + "<br><br>";
    int i = 1;
    for(Pair<String, String> p : l){
      if(i == l.size() - 1) {
        ret += "line : " + p.getValue() + " : " + p.getObj() + " -> " + l.get(i).getObj() + "<br><br>";
        ret += "Arrival : " + to;
        break;
      }
      ret += "line : " + p.getValue() + " : " + p.getObj() + " -> " + l.get(i).getObj() + "<br>";
      i++;
    }
    return ret;
  }

  public static String time(Double time) {
    if (time == Double.POSITIVE_INFINITY) throw new IllegalArgumentException();
    Double seconds = time % 60;
    Double minutesTmp = (time - seconds) / 60;
    Double minutes = minutesTmp % 60;
    Double hours = (minutesTmp - minutes) / 60;

        return "Average time to get to your destination : " + hours + " h, "  + minutes + " min, " + seconds + " s.";
  }


  public static String perturbationToHtml(String city) {
    Set<String> set = Trafics.getPerturbation(city);
    if (set.isEmpty()) return "";
    String res = "<h4>Here are the actual perturbation, select the one you want to remove</h4>\n" +
    "<form action=\"" + city + "/removePerturbation\" method=\"post\">\n";
    for (String s : set) {
      res += "<input type=\"checkbox\" id=\"" + s + "\" value=\"" + s + "\" name=\"removePerturbation\"><label for=\"" + s + "\">" + s + "</label><br>";
    }
    res += "<input type=\"submit\">\n" +
    "</form>";
    return res;
  }

  public static String stat1(Pair<Pair<Station, Station>, Double> stat1, int stat2, String stat3, String stat4, int stat5, int stat6, Pair<String, Double> stat7, Pair<String, Double> stat8){
    Double seconds = stat1.getValue() % 60;
    Double minutesTmp = (stat1.getValue() - seconds) / 60;
    Double minutes = minutesTmp % 60;
    Double hours = (minutesTmp - minutes) / 60;
    int seconds5 = stat5 % 60;
    int minutesTmp5 = (stat5 - seconds5) / 60;
    int minutes5 = minutesTmp5 % 60;
    int hours5 = (minutesTmp5 - minutes5) / 60;
    Double seconds7 = stat7.getValue() % 60;
    Double minutesTmp7 = (stat7.getValue() - seconds7) / 60;
    Double minutes7 = minutesTmp7 % 60;
    Double hours7 = (minutesTmp7 - minutes7) / 60;
    Double seconds8 = stat8.getValue() % 60;
    Double minutesTmp8 = (stat8.getValue() - seconds8) / 60;
    Double minutes8 = minutesTmp8 % 60;
    Double hours8 = (minutesTmp8 - minutes8) / 60;
    return "<h3>The longest traject between two stations :</h3>Traject : " + stat1.getObj().getObj() + " to " + stat1.getObj().getValue()
    + "<br>Time : " + hours + "h "+ minutes + "min " + seconds + "sec<br><h3>Number minimum of correspondence to do all the possible trajects on the network : " + stat2 +"</h3><br><h3> The average number of stations per line : " + stat6 + "</h3><br><h3>The line with the most stations : line " + stat3 + "</h3>"
    + "<br><h3>The line with the least stations : line " + stat4 + "</h3><br><h3>The average time from one terminus of a line to the other : " + hours5 + "h "+ minutes5 + "min " + seconds5 + "sec</h3><br><h3>The longest(duration) line : line " + stat7.getObj() + " in " + hours7 + "h "+ minutes7 + "min " + seconds7 + "sec"
    + "</h3><br><h3>The shortest(duration) line : line " + stat8.getObj() + " in "+ hours8 + "h "+ minutes8 + "min " + seconds8 + "sec</h3><br>" ;
  }

  public static String multiplePath(WGraph<Station> g, Station start, Station to) {
    TreeSet<Pair<String, Double>> resAux = new TreeSet<Pair<String, Double>>((p1, p2) -> {
      if (p1.getValue() < p2.getValue()) return -1;
      if (p1.getValue() > p2.getValue()) return 1;
      else {
        return p1.getObj().hashCode() - p2.getObj().hashCode();
      }
    });
    double THRESHOLD = 1.2;
    HashMap<Station, Station> prev = new HashMap<Station, Station>();
    HashMap<Station, Double> dist = new HashMap<Station, Double>();
    ArrayList<Pair<Station, Station>> changingStation  = new ArrayList<Pair<Station, Station>>();
    Dijkstra.shortestPath(g, start, prev, dist);
      try {
      resAux.add(new Pair<String, Double>(
        "<h2>Time</h2>\n" +
        time(dist.get(to)) +
        "<h2>Itinerary</h2>" +
        path(prev, to, changingStation),
        dist.get(to)
      ));

    } catch(RuntimeException e) {
      return "Due to actual trafics perturbation we couldn't find any path from " + start.getName() + " to " + to.getName();
    }

    multiplePathAux(g, start, to, changingStation, resAux, THRESHOLD * dist.get(to), 0);

    String res = "";
    for (Pair<String, Double> p : resAux) {
      res += p.getObj();
    }
    return res;
  }

  private static void multiplePathAux(WGraph<Station> g, Station start, Station to, ArrayList<Pair<Station, Station>> changingStation, TreeSet<Pair<String, Double>> resAux, Double threshold, int depth) {
    int MAX_CORRESPONDANCES = 3;
    int MAX_DEPTH = 5;
    if (depth >= MAX_DEPTH) return;

    HashMap<Station, Station> prev = new HashMap<Station, Station>();
    HashMap<Station, Double> dist = new HashMap<Station, Double>();
    for (Pair<Station, Station> p : changingStation) {
      WGraph<Station> revert = new WGraph<Station>();
      Station st = p.getObj();
      revert.addVertex(st);
      for (Station n : g.neighbors(st)) {
        if (n.getName().equals(st.getName()) && ! n.getLine().startsWith("Meta Station")) {
          // Changin are on both sides
          revert.addVertex(n);
          revert.addEdge(st, n, g.weight(st, n));
          g.setWeight(st, n, Double.POSITIVE_INFINITY);
        }
      }
      ArrayList<Pair<Station, Station>> pChangingStation = new ArrayList<Pair<Station, Station>>();
      Dijkstra.shortestPath(g, start, prev, dist);
      if (dist.get(to) <= threshold) {
        String itinerary = path(prev, to, pChangingStation);
        if (pChangingStation.size() <= MAX_CORRESPONDANCES) {
          resAux.add(new Pair<String, Double>(
          "<h2>Time</h2>\n" +
          time(dist.get(to)) +
          "<h2>Itinerary</h2>" +
          itinerary,
          dist.get(to)
          ));
          multiplePathAux(g, start, to, pChangingStation, resAux, threshold, depth + 1);
        }

      }
      g.apply(revert);
    }
  }

}
