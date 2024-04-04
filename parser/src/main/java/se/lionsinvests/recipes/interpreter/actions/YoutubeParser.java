package se.lionsinvests.recipes.interpreter.actions;

import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.ActionIdentifier;
import se.lionsinvests.recipes.sdk.Ingredient;

public class YoutubeParser implements ActionParserInterface {
    @Override
    public Action parse(String instructionString) {
        return new Action(ActionIdentifier.DIVIDER, new Ingredient[0], instructionString);
    }
}
