package se.lionsinvests.recipes.renderer.translation;

import lombok.AllArgsConstructor;
import se.lionsinvests.recipes.renderer.Translator;
import se.lionsinvests.recipes.renderer.UnitTranslator;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.Ingredient;

@AllArgsConstructor
public class MixActionTranslator implements Translator<Action> {

    private final UnitTranslator unitTranslator;

    @Override
    public String translate(Action action) {
        Ingredient[] ingredients = action.getIngredients();
        StringBuilder stringBuilder = new StringBuilder("Mix ");
        for (int i = 0; i < ingredients.length; i++) {
            if (i > 0 && i < ingredients.length - 1) {
                stringBuilder.append(", ");
            } else if (i > 0) {
                stringBuilder.append(" and ");
            }
            stringBuilder.append(getIngredientAndQuantity(ingredients[i]));
        }
        stringBuilder.append(" in a bowl");
        return stringBuilder.toString();
    }

    private String getIngredientAndQuantity(Ingredient ingredient) {
        String translatedUnit = unitTranslator.translate(ingredient.getUnit());
        return ingredient.getQuantity() + " " + translatedUnit + " of " + ingredient.getDescription();
    }
}
