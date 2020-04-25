package fr.univparis.metro;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.util.HashMap;
import org.json.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Set;

public class Configuration{

    /**
     * Each key is the name of a city, the value is the name of the corresponding file that contains the metro plan
     */
    private static HashMap<String, String> cities = new HashMap<String, String>();

    public static String getFileName(String s){return cities.get(s);}

    public static Set<String> getCitiesName() {
      return cities.keySet();
    }

    /**
     * @param f is a json file that contains an array of pair city/file.
     * Fills the HashMap cities.
     */
    public static void loadFrom (InputStream ins){
        try{
            JSONObject json = new JSONObject(IOUtils.toString(ins, "utf8"));
            JSONArray jArray = json.getJSONArray("cities");
            JSONObject j;
            for(int i = 0; i<jArray.length(); i++){
                j = jArray.getJSONObject(i);
                String city = j.getString("city");
                String metroPlan = j.getString("metroPlan");
                cities.put(city, metroPlan);
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}
