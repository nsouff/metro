package fr.univparis.metro;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConfigurationTest{

    @Test
    public void loadFromTest(){
        Configuration.loadFrom(ConfigurationTest.class.getResourceAsStream("/cities.json"));
        assertEquals("liste.txt", Configuration.getFileName("Paris"));
    }
}
