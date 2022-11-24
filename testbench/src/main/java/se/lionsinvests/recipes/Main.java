package se.lionsinvests.recipes;

import se.lionsinvests.recipes.interpreter.Interpreter;
import se.lionsinvests.recipes.renderer.*;
import se.lionsinvests.recipes.sdk.Recipe;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws Exception {
        Objects.requireNonNull(args);
        Objects.requireNonNull(args[0]);
        Objects.requireNonNull(args[1]);

        File recipesFolder = new File(args[0]);
        File outputFolder = new File(args[1]);
        File recipeTemplate = File.createTempFile("recipes-", ".html");
        recipeTemplate.deleteOnExit();

        if (!recipesFolder.exists() || !recipesFolder.isDirectory()) {
            throw new IllegalStateException("could not find recipes folder or is not a folder");
        }

        if (outputFolder.exists()) {
            FileUtil.deleteDirectoryStream(outputFolder.toPath());
        }

        if (!outputFolder.mkdir()) {
            throw new IllegalStateException("failed to create output folder '" + outputFolder.getAbsolutePath() + "'");
        }

        exportResource("/style.css", new File(outputFolder, "style.css"));
        exportResource("/empty.png", new File(outputFolder, "empty.png"));
        exportResource("/recipe.html", recipeTemplate);

        List<HtmlFrontpageRenderer.RecipeLink> recipeLinkList = new ArrayList<>();

        Interpreter interpreter = new Interpreter();
        UnitTranslator unitTranslator = new UnitTranslator();
        ActionTranslator actionTranslator = new ActionTranslator(unitTranslator);

        Files.find(Paths.get(recipesFolder.getAbsolutePath()), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile()).forEach(recipeFile -> {
            Recipe recipe;
            try {
                recipe = interpreter.interpret(recipeFile.toFile());
            } catch (Exception e) {
                throw new IllegalStateException("failed to interpret file " + recipeFile.toString(), e);
            }
            PageRenderer<Recipe> pageRenderer = new ThymeleafPageRenderer(actionTranslator, unitTranslator, recipeTemplate);

            String html = pageRenderer.render(recipe);
            String targetFileName = getTargetFileName(recipeFile.toFile().getName());
            File file = new File(outputFolder, targetFileName);
            try {
                writeToFile(file, html);
            } catch (IOException e) {
                throw new IllegalStateException("failed to render file " + file.getAbsolutePath(), e);
            }
            System.out.println("rendered " + targetFileName);

            HtmlFrontpageRenderer.RecipeLink recipeLink = new HtmlFrontpageRenderer.RecipeLink(targetFileName, recipe.getMetadata().getName(), recipe.getMetadata().getImages().get(0));
            recipeLinkList.add(recipeLink);
        });

        HtmlFrontpageRenderer htmlFrontpageRenderer = new HtmlFrontpageRenderer(new HtmlTemplate("Recipes"), recipeLinkList);
        String indexHtml = htmlFrontpageRenderer.render();
        File index = new File(outputFolder, "index.html");
        writeToFile(index, indexHtml);

    }

    private static String getTargetFileName(String originalFileName) {
        return originalFileName + ".html";
    }

    private static void writeToFile(File file, String content) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] strToBytes = content.getBytes();
            outputStream.write(strToBytes);
        }
    }

    private static void exportResource(String resourceName, File outputFile) throws Exception {
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
