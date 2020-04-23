package fr.univparis.metro;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

//Class used for the autocompletion of the webserver
public class ListStation{

    /**
     * @param name the name of the subaway (Paris, Lille ...)
     * @return a string representation of a javascript array containing all the stations
     * @throws FileNotFoundException
     */
    public static String getListStation(String name) throws FileNotFoundException{
        String str = "[";
        ArrayList<String> l = new ArrayList<>();
        try(Scanner sc = new Scanner(new File("src/main/resources/liste.txt"))){
            while(sc.hasNext()){
                String line = sc.nextLine();
                if(!line.startsWith("Ligne") && !line.equals("[") && !line.equals("]") && !line.equals("{") && !line.equals("}") && !line.equals("--") && !line.isEmpty() && !line.equals("||")){
                    if(!l.contains(line)){
                        str += "\"" + line + "\", ";
                        l.add(line);
                    }
                }
            }
        }
        String ret = "";
        for(int i = 0; i < str.length() - 2; i++) ret += str.charAt(i);
        ret += "]";
        return ret;
    }

}