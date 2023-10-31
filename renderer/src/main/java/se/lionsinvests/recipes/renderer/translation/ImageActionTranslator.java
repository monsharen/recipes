package se.lionsinvests.recipes.renderer.translation;

import se.lionsinvests.recipes.renderer.Translator;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.Ingredient;

public class ImageActionTranslator implements Translator<Action> {
    @Override
    public String translate(Action action) {
        Ingredient[] ingredients = action.getIngredients();
        String html = "<img src=\"" +  ingredients[0].description + "\" class='img-fluid rounded mx-auto d-block' alt='" + ingredients[0].description + "' /><br/> ";

        if (ingredients.length == 2) {
            html += ingredients[1].description;
        }

        return html;
    }
}
