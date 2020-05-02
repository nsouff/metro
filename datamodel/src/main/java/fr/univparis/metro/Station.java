package fr.univparis.metro;

import java.util.Objects;

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
    /**
    * Return the description of the station
    * @return a string for the description of the station
    */
    public String toString() {
	return (!line.startsWith("Meta Station")) ? "Station: " + name + ", " + line : name;
    }

    @Override
    /**
    * Return the hashing value of the station
    * @return the hashing value of the station
    */
    public int hashCode() {
	return Objects.hash(line, name);
    }

    @Override
    /**
    * Compare the station with an object
    * @param obj the object to compare with the station
    * @return true if there are equals else false
    */
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Station other = (Station) obj;
	return Objects.equals(line, other.line) && Objects.equals(name, other.name);
    }

    /**
     * Return true if s and this station have the same name
     * @param s The station we want to test if they have the same name
     * @return true if and only if s.name.equals(name)
     */
    public boolean sameName(Station s) {return s.name.equals(name);}
}
