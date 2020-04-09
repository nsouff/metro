package fr.univparis.metro;

import java.util.*;

public class WebserverLib {
  public static String toOption() {
    String res = "";
    boolean first = true;
    for (String s : Configuration.getCitiesName()) {
      res += "<option " + ((first)? "selected": "") + " value = \"" + s + "\">" + s + "</option>\n";
    }
    return res;
  }

  public static String path(HashMap<Station, Station> prev, Station to) {
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
    for (Station st : path) {
      if (!st.getLine().equals(line)) {
        res += st.getName() + "<br>" + "line " + st.getLine() + " : " + st.getName() + " -> ";
        line = st.getLine();
      }
    }
    res += to.getName() + "<br><br>Arrival: " + to.getName();
    return res;
  }

  public static String time(Double time) {
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

  public static String stat1(Pair<Pair<Station, Station>, Double> stat1, int stat2, String stat3, String stat4, int stat5, int stat6, String stat7, String stat8){
    Double seconds = stat1.getValue() % 60;
    Double minutesTmp = (stat1.getValue() - seconds) / 60;
    Double minutes = minutesTmp % 60;
    Double hours = (minutesTmp - minutes) / 60;
    int seconds5 = stat5 % 60;
    int minutesTmp5 = (stat5 - seconds5) / 60;
    int minutes5 = minutesTmp5 % 60;
    int hours5 = (minutesTmp5 - minutes5) / 60;
    return "<h3>The longest traject between two stations :</h3>Traject : " + stat1.getObj().getObj() + " to " + stat1.getObj().getValue()
    + "<br>Time : " + hours + "h "+ minutes + "min " + seconds + "sec<br><h3>Number minimum of correspondence to do all the trajects possible on the network : " + stat2 +"</h3><br><h3> The average number of stations per line : " + stat6 + "</h3><br><h3>The line with the most stations : line " + stat3 + "</h3>"
    + "<br><h3>The line with the less stations : line " + stat4 + "</h3><br><h3>The average time on the lines : " + hours5 + "h "+ minutes5 + "min " + seconds5 + "sec</h3><br><h3>The longest(duration) line : line " + stat7
    + "</h3><br><h3>The shortest(duration) line : line " + stat8 + "</h3><br>" ;
  }

}
