package se.lionsinvests.recipes;

import org.junit.jupiter.api.Test;

import java.io.File;

public class CreatePagesTest {

    @Test
    public void create() throws Exception {
        File recipesFolder = new File("../recipes");
        //File publicFolder = new File("../recipes-rendered");
        Main.main(new String[] { recipesFolder.getAbsolutePath() });
    }
}
