package fr.univparis.metro;

public class Station {
  private final String name;


  Station(String s) {
    name = s;
  }

  @Override
  public String toString() {
    return "Station: " + name;
  }

  @Override
  public boolean equals(Object o) {
    if (! (o instanceof Station)) return false;
    return name.equals(((Station)o).name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
