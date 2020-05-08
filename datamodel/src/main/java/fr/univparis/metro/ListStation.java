package fr.univparis.metro;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * Class used for the autocompletion of the webserver.
 * It allows us to read in text files and store the names of every stations that are in the file.
 */
public class ListStation{

  /**
   * Stores the names of stations in a string representation of a javascript array.
   * @param name the name of the subway (Paris, Lille ...).
   * @return a string representation of a javascript array containing all the stations.
  */
  public static String getListStation(String name) {
    String str = "[";
    ArrayList<String> l = new ArrayList<>();
    Scanner sc = new Scanner(ListStation.class.getResourceAsStream("/" + Configuration.getFileName(name)));
    while(sc.hasNext()){
      String line = sc.nextLine();
      if(!line.startsWith("Ligne") && !line.equals("[") && !line.equals("]") && !line.equals("{") && !line.equals("}") && !line.equals("--") && !line.isEmpty() && !line.equals("||")){
        if(!l.contains(line)){
          str += "\"" + line + "\", ";
          l.add(line);
        }
      }
    }
    sc.close();
    String ret = "";
    for(int i = 0; i < str.length() - 2; i++) ret += str.charAt(i);
    ret += "]";
    return ret;
  }

}
