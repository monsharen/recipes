package se.lionsinvests.recipes.mealplan;

import lombok.extern.java.Log;
import se.lionsinvests.recipes.interpreter.Interpreter;
import se.lionsinvests.recipes.mealplan.dto.RecipeDTO;
import se.lionsinvests.recipes.sdk.Ingredient;
import se.lionsinvests.recipes.sdk.Recipe;
import se.lionsinvests.recipes.sdk.unitconversion.SwedishUnitTranslator;
import se.lionsinvests.recipes.sdk.unitconversion.UnitConverter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

@Log
public class Main {

    public static void main(String[] args) throws Exception {
        Objects.requireNonNull(args);
        Objects.requireNonNull(args[0]);
        Objects.requireNonNull(args[1]);

        File recipesFolder = new File(args[0]);
        int recipeCount = Integer.parseInt(args[1]);

        if (!recipesFolder.exists() || !recipesFolder.isDirectory()) {
            throw new IllegalStateException("could not find recipes folder or is not a folder");
        }

        File outputFolder = new File("/Users/thomas.rosenquist/git/recipes/mealplan/generated-email");

        if (outputFolder.exists()) {
            FileUtil.deleteDirectoryStream(outputFolder.toPath());
        }

        if (!outputFolder.mkdir()) {
            throw new IllegalStateException("failed to create output folder '" + outputFolder.getAbsolutePath() + "'");
        }

        Path path = recipesFolder.toPath();
        List<RecipeDTO> recipes = getRecipes(path);
        List<RecipeDTO> randomRecipesFromList = getRandomRecipesFromList(recipes, recipeCount);
        log.info("randomRecipesFromList: " + randomRecipesFromList);

        File emailTemplate = File.createTempFile("temp-", ".html");
        emailTemplate.deleteOnExit();
        exportResource("/email.html", emailTemplate);

        List<String> ingredients = getIngredients(recipes);

        ThymeleafHtmlRenderer thymeleafHtmlRenderer = new ThymeleafHtmlRenderer(emailTemplate);
        String html = thymeleafHtmlRenderer.render(randomRecipesFromList, ingredients);
        File result = new File(outputFolder, "email-generated.html");
        writeToFile(result, html);
        log.info("email generated at " + result.getAbsolutePath());

    }

    private static List<String> getIngredients(List<RecipeDTO> recipeDTOList) {
        List<String> ingredients = new ArrayList<>();

        SwedishUnitTranslator unitTranslator = new SwedishUnitTranslator();
        UnitConverter unitConverter = new UnitConverter(unitTranslator);
        for (RecipeDTO recipeDTO : recipeDTOList) {
            List<Ingredient> recipeIngredients = recipeDTO.getRecipe().getIngredients();
            for (Ingredient ingredient : recipeIngredients) {
                String res = getAmountAndUnit(unitConverter, ingredient);
                ingredients.add(res);
            }
        }

        return ingredients;
    }

    private static String getAmountAndUnit(UnitConverter unitConverter, Ingredient ingredient) {
        if (ingredient == null) {
            return "";
        }
        String unit = unitConverter.getUnitDisplayName(ingredient.unit, ingredient.getQuantity());
        return String.format("%.0f", ingredient.quantity) + " " + unit + " " + ingredient.description;
    }

    private static List<RecipeDTO> getRandomRecipesFromList(List<RecipeDTO> recipes, int count) {
        List<RecipeDTO> recipeList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            RecipeDTO recipe = removeRandomRecipe(random, recipes);
            recipeList.add(recipe);
        }
        return recipeList;
    }

    private static RecipeDTO removeRandomRecipe(Random random, List<RecipeDTO> recipes) {
        int i = random.nextInt(recipes.size());
        return recipes.remove(i);
    }

    private static List<RecipeDTO> getRecipes(Path path) throws IOException {
        List<RecipeDTO> recipeList = new ArrayList<>();
        Interpreter interpreter = new Interpreter();
        try (Stream<Path> pathStream = Files.find(path, Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile())) {
            pathStream.forEach(recipeFile -> {
                Recipe recipe = getRecipe(interpreter, recipeFile);
                if (recipe.getMetadata().getTypes().contains("Main")) {
                    RecipeDTO recipeDTO = RecipeDTO.builder()
                            .recipe(recipe)
                            .url(recipeFile.getFileName().toString())
                            .build();
                    recipeList.add(recipeDTO);
                }
            });
        }

        return recipeList;
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
