package fr.univparis.metro;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Test;
import java.net.URL;

public class ConfigurationTest{

    @Test
    public void loadFromTest(){
        URL url = this.getClass().getResource("/cities.json");
        File input = new File(url.getFile());
        Configuration.loadFrom(input);
        assertEquals("liste.txt", Configuration.getFileName("Paris"));
    }
}
