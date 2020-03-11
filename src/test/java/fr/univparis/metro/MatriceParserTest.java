package fr.univparis.metro;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;

public class MatriceParserTest{

    @Test
    public void createMatriceLineTest() throws IOException{
        MatriceParser.createMatriceLine(new File("src/main/resources/liste.txt"), "Ligne 1");
    }
}
