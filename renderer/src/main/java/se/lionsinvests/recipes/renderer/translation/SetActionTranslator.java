package se.lionsinvests.recipes.renderer.translation;

import lombok.AllArgsConstructor;
import se.lionsinvests.recipes.renderer.Translator;
import se.lionsinvests.recipes.sdk.UnitTranslator;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.Ingredient;

@AllArgsConstructor
public class SetActionTranslator implements Translator<Action> {

    private final UnitTranslator unitTranslator;

    @Override
    public String translate(Action action) {
        Ingredient ingredient = action.getIngredients()[0];
        return "Set " + ingredient.getDescription() + " at <b>" + ingredient.getQuantity() + " " + unitTranslator.translate(ingredient.getUnit()) + "</b>";
    }
}
