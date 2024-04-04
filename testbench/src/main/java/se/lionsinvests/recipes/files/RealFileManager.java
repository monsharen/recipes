package se.lionsinvests.recipes.files;

import se.lionsinvests.recipes.Main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class RealFileManager implements FileManager {

    public void deleteDirectoryStream(Path path) throws IOException {
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    public void writeToFile(File outputFolder, String targetFileName, String html) {
        File file = new File(outputFolder, targetFileName);
        try {
            writeToFile(file, html);
        } catch (IOException e) {
            throw new IllegalStateException("failed to render file " + file.getAbsolutePath(), e);
        }
    }

    public void writeToFile(File file, String content) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] strToBytes = content.getBytes();
            outputStream.write(strToBytes);
        }
    }

    public void exportResource(String resourceName, File outputFile) throws Exception {
        try (InputStream stream = Main.class.getResourceAsStream(resourceName);
             OutputStream resStreamOut = new FileOutputStream(outputFile)) {
            int readBytes;
            byte[] buffer = new byte[4096];

            while (true) {
                assert stream != null;
                if (!((readBytes = stream.read(buffer)) > 0)) break;
                resStreamOut.write(buffer, 0, readBytes);
            }
        }
    }
}
