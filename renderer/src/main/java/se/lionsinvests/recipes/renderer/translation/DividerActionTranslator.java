package se.lionsinvests.recipes.renderer.translation;

import se.lionsinvests.recipes.renderer.Translator;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.Ingredient;

public class DividerActionTranslator implements Translator<Action> {
    @Override
    public String translate(Action action) {
        return "<b>" + action.getDescription() + "</b>";
    }
}
