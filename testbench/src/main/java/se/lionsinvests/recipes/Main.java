package se.lionsinvests.recipes;

import lombok.extern.java.Log;
import se.lionsinvests.recipes.files.DryRunFileManager;
import se.lionsinvests.recipes.files.FileManager;
import se.lionsinvests.recipes.files.RealFileManager;
import se.lionsinvests.recipes.interpreter.Interpreter;
import se.lionsinvests.recipes.renderer.*;
import se.lionsinvests.recipes.renderer.dto.RecipeDTO;
import se.lionsinvests.recipes.sdk.Recipe;
import se.lionsinvests.recipes.sdk.unitconversion.SwedishUnitTranslator;
import se.lionsinvests.recipes.sdk.unitconversion.UnitConverter;
import se.lionsinvests.recipes.sdk.unitconversion.WeightUnitConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Log
public class Main {

    public static void main(String[] args) throws Exception {
        Objects.requireNonNull(args);
        if (args.length < 1) {
            log.info("Usage: java -jar testbench-1.0-SNAPSHOT.jar <recipes-folder> <output-folder> [--dry-run]");
            return;
        }

        Objects.requireNonNull(args[0], "missing recipes folder argument");

        File recipesFolder = new File(args[0]);

        File recipeTemplate = File.createTempFile("recipes-", ".html");
        recipeTemplate.deleteOnExit();

        if (!recipesFolder.exists() || !recipesFolder.isDirectory()) {
            throw new IllegalStateException("could not find recipes folder or is not a folder");
        }

        FileManager fileManager = getFileManager(args);
        fileManager.exportResource("/style.css");
        fileManager.exportResource("/empty.png");
        fileManager.exportResource("/index.html");
        fileManager.exportResource("/recipes.js");
        fileManager.exportResource("/recipe.html", recipeTemplate);

        // Progressive web app: manifest, service worker and icons.
        fileManager.exportResource("/manifest.webmanifest");
        fileManager.exportResource("/service-worker.js");
        fileManager.exportResource("/pwa.js");
        fileManager.exportResource("/icon-192.png");
        fileManager.exportResource("/icon-512.png");
        fileManager.exportResource("/apple-touch-icon.png");
        fileManager.exportResource("/favicon.png");

        List<RecipeDTO> recipeList = new ArrayList<>();

        Interpreter interpreter = new Interpreter();

        SwedishUnitTranslator unitTranslator = new SwedishUnitTranslator();
        UnitConverter unitConverter = new UnitConverter(unitTranslator);
        List<String> supportedIngredients = WeightUnitConverter.getSupportedIngredients();
        UnitHtmlRenderer unitHtmlRenderer = new UnitHtmlRenderer(unitTranslator, supportedIngredients);
        ActionTranslator actionTranslator = new ActionTranslator(unitHtmlRenderer);

        Path path = recipesFolder.toPath();
        PageRenderer<Recipe> pageRenderer = new ThymeleafRecipePageRenderer(actionTranslator, unitConverter, recipeTemplate);

        try (Stream<Path> pathStream = Files.find(path, Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile())) {
            pathStream.forEach(recipeFile -> {
                Recipe recipe = getRecipe(interpreter, recipeFile);
                String html;
                try {
                    html = pageRenderer.render(recipe);
                } catch (Exception e) {
                    throw new IllegalStateException("failed to render recipe " + recipeFile.toFile().getName(), e);
                }

                String targetFileName = getTargetFileName(recipeFile.toFile().getName());
                fileManager.writeRecipeToFile(targetFileName, html);
                log.info("rendered " + targetFileName);

                RecipeDTO recipeDto = map(recipe, targetFileName);
                recipeList.add(recipeDto);
            });
        }

        // TODO: Generate recipes.json
        JsonRenderer jsonRenderer = new JsonRenderer(recipeList);
        String json = jsonRenderer.render();
        fileManager.writeToFile("recipes.json", json);
        log.info("created recipes.json containing " + recipeList.size() + " recipes");
    }

    private static FileManager getFileManager(String[] args) throws IOException {

        if (isDryRun(args)) {
            log.info("running in dry-run mode. won't save files to disk");
            return new DryRunFileManager();
        }

        Objects.requireNonNull(args[1], "missing output folder argument");
        File outputFolder = new File(args[1]);

        if (outputFolder.exists()) {
            RealFileManager.deleteDirectoryStream(outputFolder.toPath());
        }

        if (!outputFolder.mkdir()) {
            throw new IllegalStateException("failed to create output folder '" + outputFolder.getAbsolutePath() + "'");
        }

        return new RealFileManager(outputFolder);
    }

    private static boolean isDryRun(String[] args) {

        if (args.length < 2) {
            return true;
        }

        for (String arg : args) {
            if (arg.equals("--dry-run")) {
                return true;
            }
        }

        return false;
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
}
