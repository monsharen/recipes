package se.lionsinvests.recipes.files;

import java.io.File;

public interface FileManager {

    void writeToFile(String targetFileName, String html);

    void writeRecipeToFile(String targetFileName, String html);

    void exportResource(String resourceName) throws Exception;
    void exportResource(String resourceName, File outputFile) throws Exception;
}
