package fr.univparis.metro;
import java.util.*;


public class Station {
  final String name;

  // We won't keep it
  static ArrayList<Station> stations = new ArrayList<Station>();

  Station(String n) {
    name = n;
  }

  public String toString() {
    return "Station: " + name;
  }
}
