package fr.univparis.metro;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

public class Initialize{

    /**
     * @param g
     * @return an ArrayList that contains all the name of the sations of the graph g.
     */
    private static ArrayList<String> getStationsName(WGraph<Station> g){
        Set<Station> stations = g.getVertices();
        ArrayList<String> stationsName = new ArrayList<String>();
        for(Station s : stations) stationsName.add(s.getName());
        return stationsName;
    }

    /**
     * Ask the user in which city he is.
     * @return a SimpleEntry: the key is the name of a city, the value is the pathname of the file corresponding to that city.
     */
    public static SimpleEntry<String, String> whichCity(){
        Scanner sc = new Scanner(System.in);
        System.out.println("In which city are you ?");
        String str = sc.nextLine();
        while(!Configuration.getCities().containsKey(str)){
            System.out.println("Wrong city name, try again");
            str = sc.nextLine();
        }
        SimpleEntry<String, String> ret = new SimpleEntry<String, String>(str, Configuration.getCities().get(str));
        return ret;
    }

    /**
     * Ask the user from where he starts his route.
     * @param g a graph corresponding to the city the user is in.
     * @return the station name of the begining of his route.
     */
    public static String whereFrom(WGraph<Station> g){
        ArrayList<String> stationsName = getStationsName(g);
        Scanner sc = new Scanner(System.in);
        System.out.println("Where are you ?");
        String str = sc.nextLine();
        while(!stationsName.contains(str)){
            System.out.println("This subway station does not exist, where are you ?");
            str = sc.nextLine();
        }
        return str;
    }

    /**
     * Ask the user where he wants to go.
     * @param g a graph corresponding to the city the user is in.
     * @return the station name of the end of his route.
     */
    public static String whereTo(WGraph<Station> g){
        ArrayList<String> stationsName = getStationsName(g);
        Scanner sc = new Scanner(System.in);
        System.out.println("Where do you want to go ?");
        String str = sc.nextLine();
        while(!stationsName.contains(str)){
            System.out.println("Your destination does not exist, try again");
            str = sc.nextLine();
        }
        return str;
    }

    public static void initialize() throws IOException {
        SimpleEntry<String, String> s = whichCity();
        WGraph<Station> g = Parser.loadFrom(new File(s.getValue()));
        String from = whereFrom(g);
        String to = whereTo(g);
    }
}