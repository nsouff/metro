package fr.univparis.metro;

public class Station {
  private final String name;
  private final String line;

 /**
  * Create a station name n in the line l
  * @param n The name of the station
  * @param l The line of the station
  * @return The station named n in the line l
  */
  Station(String n, String l) {
    if (l == null || l.isEmpty() || n == null || n.isEmpty())
      throw new IllegalArgumentException("Station name and line can't be null or empty");
    name = n;
    line = l;
  }

 /**
  * Return the name of the station
  * @return the name of the station
  */
  public String getName() {return name;}

 /**
  * Return the line of the station
  * @return the line of the station
  */
  public String getLine() {return line;}

  @Override
  public String toString() {
    return (!line.startsWith("Meta Station")) ? "Station: " + name + ", " + line : name;
  }

  @Override
  public boolean equals(Object o) {
    if (! (o instanceof Station)) return false;
    return name.equals(((Station)o).name) && line.equals(((Station)o).line);
  }

  @Override
  public int hashCode() {
    int res = 1;
    int prime = 31;
    res = prime * res + name.hashCode();
    res = prime * res + ((line == null) ? 0 : line.hashCode());
    return name.hashCode();
  }

 /**
  * Return true if s and this station have the same name
  * @param s The station we want to test if they have the same name
  * @return true if and only if s.name.equals(name)
  */
  public boolean sameName(Station s) {return s.name.equals(name);}
}
