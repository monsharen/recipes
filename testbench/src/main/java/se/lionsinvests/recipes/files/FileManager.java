package se.lionsinvests.recipes.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface FileManager {

    void deleteDirectoryStream(Path path) throws IOException;

    void writeToFile(File outputFolder, String targetFileName, String html);

    void writeToFile(File file, String content) throws IOException;

    void exportResource(String resourceName, File outputFile) throws Exception;
}
