package fr.univparis.metro;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Set;

public class Initialize{

    private static ArrayList<String> getStationsName(WGraph<Station> g){
        Set<Station> stations = g.getVertices();
        ArrayList<String> stationsName = new ArrayList<String>();
        for(Station s : stations) stationsName.add(s.getName());
        return stationsName;
    }

    public static String whichCity(){
        Scanner sc = new Scanner(System.in);
        System.out.println("In which city are you ?");
        String str = sc.nextLine();
        while(!Configuration.getCities().containsKey(str)){
            System.out.println("Wrong city name, try again");
            str = sc.nextLine();
        }
        return str;
    }

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

    public static String[] store(WGraph<Station> g){
        String[] ret = {whichCity(), whereFrom(g), whereTo(g)};
        return ret;
    }

    public static void initialize(WGraph<Station> g){
        String[] t = store(g);
    }
}