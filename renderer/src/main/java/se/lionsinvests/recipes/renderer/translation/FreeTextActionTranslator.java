package se.lionsinvests.recipes.renderer.translation;

import se.lionsinvests.recipes.renderer.Translator;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.Ingredient;

public class FreeTextActionTranslator implements Translator<Action> {
    @Override
    public String translate(Action action) {
        Ingredient[] ingredients = action.getIngredients();
        return ingredients[0].description;
    }
}
