package se.lionsinvests.recipes.renderer;

import se.lionsinvests.recipes.renderer.translation.*;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.ActionIdentifier;
import se.lionsinvests.recipes.sdk.Recipe;
import se.lionsinvests.recipes.sdk.unitconversion.UnitConverter;

import java.util.HashMap;
import java.util.Map;

import static se.lionsinvests.recipes.sdk.ActionIdentifier.*;


public class ActionTranslator {

    private Map<ActionIdentifier, Translator<Action>> TRANSLATORS = new HashMap<>();

    public ActionTranslator(UnitConverter unitConverter, UnitHtmlRenderer unitHtmlRenderer) {
        TRANSLATORS.put(FREE_TEXT, new FreeTextActionTranslator(unitHtmlRenderer));
        TRANSLATORS.put(SET, new SetActionTranslator(unitConverter));
        TRANSLATORS.put(IMAGE, new ImageActionTranslator());
        TRANSLATORS.put(YOUTUBE, new YoutubeActionTranslator());
        TRANSLATORS.put(DIVIDER, new DividerActionTranslator());
    }

    public String translate(Recipe recipe, Action action) {
        Translator<Action> translator = getTranslator(action.getActionIdentifier(), recipe);
        return translator.translate(action);
    }

    private Translator<Action> getTranslator(ActionIdentifier actionIdentifier, Recipe recipe) {

        Translator<Action> actionTranslator = TRANSLATORS.get(actionIdentifier);
        if (actionTranslator == null) {
            throw new IllegalArgumentException("unsupported action identifier: " + actionIdentifier);
        }

        return actionTranslator;
    }
}
