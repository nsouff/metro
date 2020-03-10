package fr.univparis.metro;

import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.HashMap;

public class MatriceParser{

    private static Double defaultWeight = 90.0;
    private static Double infiniteWeight = Double.POSITIVE_INFINITY;
    private static HashMap<Integer, Station> stations = new HashMap<Integer, Station>();
    private static int nbStations = stations.size();

    public static int[][] createMatriceLine(File f, String l) throws IOException {
        Scanner sc = new Scanner(f);
        String textLine = "";
        String currentLine = "";
        int nbStationsInLine = 0;
        int numStation = nbStations;
        while(sc.hasNextLine()){
            textLine = sc.nextLine();
            if(textLine.isEmpty() || textLine.equals("--")) continue;
            if(textLine.startsWith("Ligne")) {currentLine = textLine; continue;}
            if(currentLine.equals(l)){
                //FIXME: complete each case
                if(textLine.equals("{")) continue;
                else if(textLine.equals("[")) continue;
                else if(textLine.equals("}")) continue;
                else if(textLine.equals("]")) continue;
                else if(textLine.equals("/")) continue;
                else if(textLine.equals("||")) continue;
                else{
                    Station s = new Station(textLine, l);
                    nbStationsInLine++;
                    numStation++;
                    stations.put(numStation, s);
                }
            }
        }
        int[][] ret = new int[nbStationsInLine][nbStationsInLine];
        return ret;
    }

}