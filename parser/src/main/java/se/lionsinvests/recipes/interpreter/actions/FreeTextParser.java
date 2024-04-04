package se.lionsinvests.recipes.interpreter.actions;

import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.ActionIdentifier;
import se.lionsinvests.recipes.sdk.Ingredient;

public class FreeTextParser implements ActionParserInterface{
    @Override
    public Action parse(String instructionString) {
        Ingredient[] ingredients = new Ingredient[0];



        return new Action(ActionIdentifier.FREE_TEXT, ingredients, instructionString);
    }
}
