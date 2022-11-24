package se.lionsinvests.recipes.renderer.translation;

import se.lionsinvests.recipes.renderer.Translator;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.Ingredient;

public class CombineActionTranslator implements Translator<Action> {
    @Override
    public String translate(Action action) {
        Ingredient[] ingredients = action.getIngredients();
        StringBuilder stringBuilder = new StringBuilder("Combine ");
        for (int i = 0; i < ingredients.length; i++) {
            if (i > 0 && i < ingredients.length - 1) {
                stringBuilder.append(", ");
            } else if (i > 0) {
                stringBuilder.append(" and ");
            }
            stringBuilder.append(ingredients[i].getDescription());
        }
        stringBuilder.append(" in a bowl");
        return stringBuilder.toString();
    }
}
