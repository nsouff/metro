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
    Double minutesTmp = time - seconds / 60;
    Double minutes = minutesTmp % 60;
    Double hours = (minutesTmp - minutes) / 60;
    return "Average time to get to your destination : " + hours + " h, "  + minutes + " min, " + seconds + " s.";
  }
}
