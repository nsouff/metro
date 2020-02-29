package fr.univparis.metro;
import java.util.*;

public class Statistics{
  /**
  *   This class contains some functions which give statistics (thanks to informations on the graph about the networks.
  *   These statistics are simple or quite complex to implement.
  *   Simple statistics : Number of stations, the duration for a traject between two extremities of a line, the list of interesting stations to have a correspondance on all the lines.
  *   Complex statistics :- Minimum number of connections to get anywhere in the network from any station, Minimum number of connections to get anywhere in the network from any station in minimal time, The most distant stations in the network.
  */

  public static <T> int nbOfStations(WGraph<T> w){
    return w.nbVertex();
  }

  public static <T> HashMap<String, Double> timeLine(WGraph<T> w){
    HashMap<String, Double> tl = new HashMap<String, Double>();

    return tl;
  }
}
