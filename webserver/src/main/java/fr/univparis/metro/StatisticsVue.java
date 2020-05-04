package fr.univparis.metro;

import java.util.HashMap;

public class StatisticsVue {
  private static HashMap<String, String> statisticsHtml;

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
    Double[] time1 = WebserverLib.doubleToTime(stat1.getValue());
    Double[] time5 = WebserverLib.doubleToTime((double) stat5);
    Double[] time7 = WebserverLib.doubleToTime(stat7.getValue());
    Double[] time8 = WebserverLib.doubleToTime(stat8.getValue());
    return "<h3>The longest traject between two stations :</h3>Traject : " + stat1.getObj().getObj().getName() + " to " + stat1.getObj().getValue().getName()
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

}
