package fr.univparis.metro;

import java.util.Scanner;
import java.util.LinkedList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

public class Initialize{

    /**
     * @param g
     * @return a Set that contains all the name of the sations of the graph g.
     */
    private static Set<Station> getStationsName(WGraph<Station> g){
        return g.getVertices();
    }

    /**
     * Ask the user in which city he is.
     * @return a SimpleEntry: the key is the name of a city, the value is the pathname of the file corresponding to that city.
     */
    private static SimpleEntry<String, String> whichCity(){
        Scanner sc = new Scanner(System.in);
        System.out.println("In which city are you ?");
        String str = sc.nextLine();
        while(Configuration.getFileName(str) == null){
            System.out.println("Wrong city name, try again");
            str = sc.nextLine();
        }
        SimpleEntry<String, String> ret = new SimpleEntry<String, String>(str, Configuration.getFileName(str));
        return ret;
    }

    /**
     * Ask the user from where he starts his route.
     * @param g a graph corresponding to the city the user is in.
     * @return the station name of the begining of his route.
     */
    private static Station whereFrom(WGraph<Station> g){
        Set<Station> stations = getStationsName(g);
        Scanner sc = new Scanner(System.in);
        System.out.println("Where are you ?");
        String str = sc.nextLine();
        Station s = new Station(str, "Meta Station Start");
        while(!stations.contains(s)){
            System.out.println("This subway station does not exist, where are you ?");
            str = sc.nextLine();
            s = new Station(str, "Meta Station Start");
        }
        return s;
    }

    /**
     * Ask the user where he wants to go.
     * @param g a graph corresponding to the city the user is in.
     * @return the station name of the end of his route.
     */
    private static Station whereTo(WGraph<Station> g){
        Set<Station> stations = getStationsName(g);
        Scanner sc = new Scanner(System.in);
        System.out.println("Where do you want to go ?");
        String str = sc.nextLine();
        Station s = new Station(str, "Meta Station End");
        while(!stations.contains(s)){
            System.out.println("Your destination does not exist, try again");
            str = sc.nextLine();
            s = new Station(str, "Meta Station End");
        }
        return s;
    }

    /**
     * Print the path dijsktra's algorithm returns according to the destination of the user.
     */
    public static void initialize() throws IOException {
        Configuration.loadFrom(new File("src/main/resources/cities.json"));
        SimpleEntry<String, String> s = whichCity();
        WGraph<Station> g = Parser.loadFrom(new File(s.getValue()));
        Station from = whereFrom(g);
        Station to = whereTo(g);
        HashMap<Station, Station> prev = new HashMap<Station, Station>();
        HashMap<Station, Double> dist = new HashMap<Station, Double>();
        Dijkstra.shortestPath(g, from, prev, dist);
        LinkedList<Station> path = new LinkedList<Station>();
        Double travelTime = dist.get(to);
        Double seconds = travelTime % 60;
        Double minutesTmp = (travelTime - seconds) / 60;
        Double minutes = minutesTmp % 60;
        Double hours = (minutesTmp - minutes) / 60;
        Station current = to;
        while(! prev.get(current).getLine().equals("Meta Station Start") ){
          current = prev.get(current);
          path.add(current);
        }
        Collections.reverse(path);
        System.out.println("############################# TIME ##############################");
        System.out.println("Average time to get to your destination : " + hours + " h, " + minutes + " min, " + seconds + " s.\n");
        System.out.println("############################# ITINERARY ##############################");
        String line = path.getFirst().getLine();
        System.out.print("Departure : "+from+"\n\n"+"line "+line+" : "+from+" -> ");
        for (Station st : path) {
          if (!st.getLine().equals(line)){
            System.out.print(st.getName()+"\n"+"line "+st.getLine()+" : "+st.getName()+" -> ");
            line = st.getLine();
          }
        }
        System.out.println(to+"\n\nArrival : "+to);
        System.out.println("Do you want the detail of your traject ? Tap y (for yes) or n (for no) ");
        Scanner sc = new Scanner(System.in);
        if(sc.nextLine().equals("y")){
          System.out.println("#########################TRAJECT'S DETAIL###############################");
          int i = 0;
          line = path.getFirst().getLine();
          System.out.println("Departure :"+ from);
          System.out.print("\n\nline " + line + " : " + from );
          for (Station st : path) {
            if (st.getLine().equals(line)){
              System.out.print(" -> " + st.getName());
              i++;
            }else {
              line = st.getLine();
              System.out.print("  ( " + i + " stops )\n\nline " + line + " : " + st.getName());
              i = 0;
            }
          }
          System.out.println( "  ( " + i + " stops )" );
          System.out.println("\n\nArrival : " + to);
        }
    }
}
