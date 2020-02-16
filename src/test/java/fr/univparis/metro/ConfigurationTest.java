package fr.univparis.metro;

import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.Before;
import java.net.URL;

public class ConfigurationTest{

    @Test
    public void loadFromTest(){
        URL url1 = this.getClass().getResource("/cities.json");
        File input = new File(url1.getFile());
        Configuration.loadFrom(input);
        assertEquals("liste.txt", Configuration.getCities().get("Paris"));
    }
}