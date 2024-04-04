package se.lionsinvests.recipes.interpreter.actions;

import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.ActionIdentifier;
import se.lionsinvests.recipes.sdk.Ingredient;
import se.lionsinvests.recipes.sdk.Unit;

public class ImageParser implements ActionParserInterface{
    @Override
    public Action parse(String instructionString) {
        String[] parts = instructionString.split(",");

        Ingredient url = Ingredient.builder()
                .description(parts[0])
                .unit(Unit.QUANTITY)
                .quantity(0)
                .build();
        String descriptionString = "";
        if (parts.length > 1) {
            descriptionString = instructionString.substring(instructionString.indexOf(" "));
        }
        Ingredient[] ingredients = { url };
        return new Action(ActionIdentifier.IMAGE, ingredients, descriptionString);
    }
}
