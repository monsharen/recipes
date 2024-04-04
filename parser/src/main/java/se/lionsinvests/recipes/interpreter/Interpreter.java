package se.lionsinvests.recipes.interpreter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import se.lionsinvests.recipes.interpreter.actions.ActionParser;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.Ingredient;
import se.lionsinvests.recipes.sdk.Recipe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class Interpreter {

    public Recipe interpret(File file) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.findAndRegisterModules();
        objectMapper.configure(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        RecipeFile recipeFile = objectMapper.readValue(file, RecipeFile.class);

        IngredientParser ingredientParser = new IngredientParser();
        List<Ingredient> ingredients = new ArrayList<>();
        for (String ingredientString : recipeFile.getIngredients()) {
            Ingredient ingredient = ingredientParser.parse(ingredientString);
            ingredients.add(ingredient);
        }
        List<Action> actions = new ArrayList<>();
        ActionParser actionParser = new ActionParser();
        for (String actionString : recipeFile.getActions()) {
            Action action = actionParser.parse(actionString);
            actions.add(action);
        }

        return new Recipe(
                recipeFile.getMetadata(),
                ingredients,
                actions);
    }

}
