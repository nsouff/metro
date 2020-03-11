package fr.univparis.metro;

import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.HashMap;

public class MatriceParser{

    private static final Double defaultWeight = 90.0;
    private static final Double defaultChangeStationWeight = 60.0;
    private static HashMap<Integer, Station> stations = new HashMap<Integer, Station>();
    private static int nbStations = stations.size();

    public static Double[][] createMatriceLine(File f, String l) throws IOException {
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
        Double[][] ret = new Double[nbStationsInLine][nbStationsInLine];
        for(int i = 0; i<ret.length; i++){
            for(int j = 0; j<ret[i].length; j++){
                if(j == i + 1) ret[i][j] = defaultWeight;
                else if(j == i - 1) ret[i][j] = defaultWeight;
                else if(j == i) ret[i][j] = defaultChangeStationWeight;
                else ret[i][j] = Double.POSITIVE_INFINITY;
                System.out.print(ret[i][j] + " ");
            }
            System.out.println();
        }
        return ret;
    }

}