package se.lionsinvests.recipes.renderer.translation;

import lombok.AllArgsConstructor;
import se.lionsinvests.recipes.renderer.Translator;
import se.lionsinvests.recipes.renderer.UnitHtmlRenderer;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.Ingredient;

@AllArgsConstructor
public class FreeTextActionTranslator implements Translator<Action> {

    private final UnitHtmlRenderer unitHtmlRenderer;
    public String translate(Action action) {
        Ingredient[] ingredients = action.getIngredients();
        return unitHtmlRenderer.reformatQuantitiesAndUnits(ingredients[0].description);
    }

}
