package se.lionsinvests.recipes;

import se.lionsinvests.recipes.interpreter.Interpreter;
import se.lionsinvests.recipes.renderer.*;
import se.lionsinvests.recipes.renderer.dto.RecipeDTO;
import se.lionsinvests.recipes.sdk.Recipe;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
        exportResource("/index.html", new File(outputFolder, "index.html"));
        exportResource("/recipes.js", new File(outputFolder, "recipes.js"));
        exportResource("/recipe.html", recipeTemplate);

        List<RecipeDTO> recipeList = new ArrayList<>();

        Interpreter interpreter = new Interpreter();
        UnitTranslator unitTranslator = new UnitTranslator();
        ActionTranslator actionTranslator = new ActionTranslator(unitTranslator);

        Path path = recipesFolder.toPath();
        PageRenderer<Recipe> pageRenderer = new ThymeleafRecipePageRenderer(actionTranslator, unitTranslator, recipeTemplate);

        try (Stream<Path> pathStream = Files.find(path, Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile())) {
            pathStream.forEach(recipeFile -> {
                Recipe recipe = getRecipe(interpreter, recipeFile);
                String html = pageRenderer.render(recipe);
                String targetFileName = getTargetFileName(recipeFile.toFile().getName());
                writeRecipeToFile(outputFolder, targetFileName, html);
                System.out.println("rendered " + targetFileName);

                RecipeDTO recipeDto = map(recipe, targetFileName);
                recipeList.add(recipeDto);
            });
        }

        // TODO: Generate recipes.json
        JsonRenderer jsonRenderer = new JsonRenderer(recipeList);
        String json = jsonRenderer.render();
        File recipesJson = new File(outputFolder, "recipes.json");
        writeToFile(recipesJson, json);
    }

    private static RecipeDTO map(Recipe recipe, String fileName) {
        return RecipeDTO.builder()
                .name(recipe.getMetadata().getName())
                .description(recipe.getMetadata().getDescription())
                .image(recipe.getMetadata().getImages().get(0))
                .types(recipe.getMetadata().getTypes())
                .url(fileName)
                .estimatedPrepTime(recipe.getMetadata().getEstimatedPrepTime())
                .servings(recipe.getMetadata().getServings())
                .build();
    }

    private static void writeRecipeToFile(File outputFolder, String targetFileName, String html) {
        File file = new File(outputFolder, targetFileName);
        try {
            writeToFile(file, html);
        } catch (IOException e) {
            throw new IllegalStateException("failed to render file " + file.getAbsolutePath(), e);
        }
    }

    private static Recipe getRecipe(Interpreter interpreter, Path path) {
        try {
            return interpreter.interpret(path.toFile());
        } catch (Exception e) {
            throw new IllegalStateException("failed to interpret file " + path.toString(), e);
        }
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
