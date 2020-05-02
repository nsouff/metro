package fr.univparis.metro;

import java.util.*;

public class WebserverLib {


  private static HashMap<String, String> statisticsHtml;


  public static String toOption() {
    String res = "";
    boolean first = true;
    for (String s : Configuration.getCitiesName()) {
      res += "<option " + ((first)? "selected": "") + " value = \"" + s + "\">" + s + "</option>\n";
    }
    return res;
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







  public static void initStatistics() {
    statisticsHtml = new HashMap<String, String>();
    for (String city : Configuration.getCitiesName()) {
      WGraph<Station> g = Trafics.getInitialGraph(city);
      Pair<Pair<Station, Station>, Double> stat1 = Statistics.mostDistantStations(g, (s -> !s.getLine().equals("Meta Station Start")), (t -> t.getLine().equals("Meta Station End")));
      int stat2 = Statistics.minimumCorrespondence(g, (s -> s.getLine().equals("Meta Station Start")), s -> s.getLine().equals("Meta Station End") , (Station s1, Station s2) -> s1.getLine().equals(s2.getLine()) || s1.getLine().startsWith("Meta Station") || s2.getLine().startsWith("Meta Station"));
      String stat3 = Statistics.extremumLine(g, true);
      String stat4 = Statistics.extremumLine(g, false);
      HashMap<String, Double> res = new HashMap<String, Double>();
      int stat5 = Statistics.averageTimeOnEachLine(g, res);
      int stat6 = Statistics.averageNbOfStationPerLine(g);
      Pair<String, Double> stat7 = Statistics.longestTimeTravelLine(g);
      Pair<String, Double> stat8 = Statistics.shortestTimeTravelLine(g);
      statisticsHtml.put(city, statsToString(stat1, stat2, stat3, stat4, stat5, stat6, stat7, stat8));
    }
  }


  private static String statsToString(Pair<Pair<Station, Station>, Double> stat1, int stat2, String stat3, String stat4, int stat5, int stat6, Pair<String, Double> stat7, Pair<String, Double> stat8) {
    Double[] time1 = doubleToTime(stat1.getValue());
    Double[] time5 = doubleToTime((double) stat5);
    Double[] time7 = doubleToTime(stat7.getValue());
    Double[] time8 = doubleToTime(stat8.getValue());
    return "<h3>The longest traject between two stations :</h3>Traject : " + stat1.getObj().getObj() + " to " + stat1.getObj().getValue()
    + "<br>Time : " + time1[0] + "h "+ time1[1] + "min " + time1[2] + "sec<br>" +
    "<h3>Number minimum of correspondence to do all the possible trajects on the network : " + stat2 +"</h3><br>" +
    "<h3> The average number of stations per line : " + stat6 + "</h3><br><h3>The line with the most stations : line " + stat3 + "</h3><br>" +
    "<h3>The line with the least stations : line " + stat4 + "</h3><br>" +
    "<h3>The average time from one terminus of a line to the other : " + time5[0] + "h "+ time5[1] + "min " + time5[2] + "sec</h3><br>" +
    "<h3>The longest(duration) line : line " + stat7.getObj() + " in " + time7[0] + "h "+ time7[1] + "min " + time7[2] + "sec</h3><br>" +
    "<h3>The shortest(duration) line : line " + stat8.getObj() + " in "+ time8[0] + "h "+ time8[1] + "min " + time8[2] + "sec</h3><br>" ;

  }
 /**
  * Return a String containing information about the network of city
  * @param city the city in which we want statistics
  * @return a String containing information about the network of city
  */
  public static String getStringStatistics(String city) {
    return statisticsHtml.get(city);
  }

  public static Double[] doubleToTime(Double time) {
    Double seconds = time % 60;
    Double minutesTmp = (time - seconds) / 60;
    Double minutes = minutesTmp % 60;
    Double hours = (minutesTmp - minutes) / 60;
    Double[] res = {hours, minutes, seconds};
    return res;
  }


}
