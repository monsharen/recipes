package se.lionsinvests.recipes.files;

public interface FileManager {

    void writeToFile(String targetFileName, String html);

    void exportResource(String resourceName) throws Exception;
}
