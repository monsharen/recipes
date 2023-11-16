package se.lionsinvests.recipes.renderer.translation;

import lombok.AllArgsConstructor;
import se.lionsinvests.recipes.renderer.Translator;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.Ingredient;
import se.lionsinvests.recipes.sdk.Recipe;

import java.util.regex.Pattern;

@AllArgsConstructor
public class FreeTextActionTranslator implements Translator<Action> {

    private final Recipe recipe;
    public String translate(Action action) {
        Ingredient[] ingredients = action.getIngredients();
        return ingredients[0].description;
    }


}
