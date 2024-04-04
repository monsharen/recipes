package se.lionsinvests.recipes.files;

import lombok.AllArgsConstructor;
import se.lionsinvests.recipes.Main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

@AllArgsConstructor
public class RealFileManager implements FileManager {

    private final File outputFolder;

    public static void deleteDirectoryStream(Path path) throws IOException {
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    public void writeToFile(String fileName, String content) {
        File file = new File(outputFolder, fileName);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] strToBytes = content.getBytes();
            outputStream.write(strToBytes);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("could not find file", e);
        } catch (IOException e) {
            throw new IllegalStateException("could not write to file", e);
        }
    }

    public void exportResource(String resourceName) throws Exception {

        File file = new File(outputFolder, resourceName);

        try (InputStream stream = Main.class.getResourceAsStream(resourceName);
             OutputStream resStreamOut = new FileOutputStream(file)) {
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
