package se.lionsinvests.recipes.renderer.translation;

import lombok.AllArgsConstructor;
import se.lionsinvests.recipes.renderer.Translator;
import se.lionsinvests.recipes.renderer.UnitHtmlRenderer;
import se.lionsinvests.recipes.sdk.Action;

@AllArgsConstructor
public class FreeTextActionTranslator implements Translator<Action> {

    private final UnitHtmlRenderer unitHtmlRenderer;
    public String translate(Action action) {
        String result = addLineEnding(action.getDescription());

        result = unitHtmlRenderer.reformatTimes(result);
        result = unitHtmlRenderer.reformatQuantitiesAndUnits(result);
        result = unitHtmlRenderer.reformatTemperature(result);
        return result;
    }

    private String addLineEnding(String value) {
        if (!value.endsWith(".")) {
            return value + ".";
        }

        return value;
    }

}
