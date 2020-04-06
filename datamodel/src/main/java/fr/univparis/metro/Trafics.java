package fr.univparis.metro;

import java.io.IOException;
import java.io.File;
import java.util.HashMap;

public class Trafics {

  static HashMap<String, WGraph<Station>> trafics;

  public static void initTrafics() {
    trafics = new HashMap<String, WGraph<Station>> ();
    for (String city : Configuration.getCitiesName()) {
      try {
        trafics.put(city, Parser.loadFrom(new File(Configuration.getFileName(city))));
      } catch(IOException e) {
        e.printStackTrace();
      }
    }
  }

}
