package se.lionsinvests.recipes.renderer.translation;

import se.lionsinvests.recipes.renderer.Translator;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.Ingredient;

public class ImageActionTranslator implements Translator<Action> {
    @Override
    public String translate(Action action) {
        Ingredient[] ingredients = action.getIngredients();
        String caption = action.getDescription();

        return "<figure class=\"figure\">\n" +
                "  <img src=\"" + ingredients[0].description + "\" class=\"figure-img img-fluid rounded\" alt=\"image\">\n" +
                "  <figcaption class=\"figure-caption\">" + caption + "</figcaption>\n" +
                "</figure>";
    }
}
