package fr.univparis.metro;

import java.util.*;
/**
 * Librairy class used to tranform format data into html String
 */
public class WebserverLib {

 /**
  * Return a html string that can be use in select where the option are the cities in the Configuration
  * @return a html string that can be use in select where the option are the cities in the Configuration
  */
  public static String toOption() {
    String res = "";
    boolean first = true;
    for (String s : Configuration.getCitiesName()) {
      res += "<option " + ((first)? "selected": "") + " value = \"" + s + "\">" + s + "</option>\n";
    }
    return res;
  }

 /**
  * Return a html description of the actual perturbation with the possibility to delete them
  * @param city the city we want the description of its perturbation
  * @return a html description of the actual perturbation with the possibility to delete them
  */
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

 /**
  * Seperate a double into 3 double as if the original double was a time in second
  * @param time a time in second
  * @return a Double array where the index 0 is the number of hours, the index 1 is the number of minutes and the index 2 the number of seconds
  */
  public static Double[] doubleToTime(Double time) {
    Double seconds = time % 60;
    Double minutesTmp = (time - seconds) / 60;
    Double minutes = minutesTmp % 60;
    Double hours = (minutesTmp - minutes) / 60;
    Double[] res = {hours, minutes, seconds};
    return res;
  }


}
