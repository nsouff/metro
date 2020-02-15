package fr.univparis.metro;

public class Station {
  private final String name;
  private final String line;


  Station(String n, String l) {
    if (l == null || l.isEmpty() || n == null || n.isEmpty())
      throw new IllegalArgumentException("Station name and line can't be null or empty");
    name = n;
    line = l;
  }

  @Override
  public String toString() {
    return "Station: " + name;
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

  public boolean sameName(Station s) {return s.name.equals(name);}
}
