package se.lionsinvests.recipes.renderer.translation;

import se.lionsinvests.recipes.renderer.Translator;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.Ingredient;

public class YoutubeActionTranslator implements Translator<Action> {
    @Override
    public String translate(Action action) {
        return "<iframe width=\"100%\" height=\"400\" " +
                "src=\"" + action.getDescription() + "\" " +
                "title=\"YouTube video player\" " +
                "frameborder=\"0\" " +
                "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" " +
                "allowfullscreen></iframe>";

    }
}
