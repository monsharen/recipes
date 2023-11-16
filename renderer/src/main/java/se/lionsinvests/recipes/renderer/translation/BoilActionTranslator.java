package se.lionsinvests.recipes.renderer.translation;

import lombok.AllArgsConstructor;
import se.lionsinvests.recipes.renderer.Translator;
import se.lionsinvests.recipes.sdk.unitconversion.UnitConverter;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.Ingredient;

@AllArgsConstructor
public class BoilActionTranslator implements Translator<Action> {

    private final UnitConverter unitConverter;

    @Override
    public String translate(Action action) {
        Ingredient ingredient = action.getIngredients()[0];
        StringBuilder stringBuilder = new StringBuilder("Boil ");
        String translatedUnit = unitConverter.getUnitDisplayName(action.getIngredients()[0].getUnit());
        stringBuilder.append(ingredient.getQuantity()).append(" ").append(translatedUnit).append(" of ").append(ingredient.getDescription());
        return stringBuilder.toString();
    }
}
