package se.lionsinvests.recipes.sdk;

import java.util.Locale;

public class ActionParser {

    public static Action parse(String value) {
        String actionString = value.substring(0, value.indexOf(" ")).toUpperCase(Locale.ROOT);
        String instructionString = value.substring(actionString.length() + 1);

        ActionIdentifier actionIdentifier = ActionIdentifier.valueOf(actionString);
        String[] parts = instructionString.split(",");

        if (ActionIdentifier.IMAGE.equals(actionIdentifier)) {
            Ingredient url = Ingredient.builder()
                    .description(parts[0])
                    .unit(Unit.QUANTITY)
                    .quantity(0)
                    .build();
            String descriptionString = "";
            if (parts.length > 1) {
                descriptionString = instructionString.substring(instructionString.indexOf(" "));
            }

            Ingredient description = Ingredient.builder()
                    .description(descriptionString)
                    .unit(Unit.QUANTITY)
                    .quantity(0)
                    .build();
            Ingredient[] ingredients = { url, description };
            return new Action(ActionIdentifier.IMAGE, ingredients);
        }

        Ingredient[] ingredients;

        ingredients = new Ingredient[parts.length];
        for (int i = 0; i < parts.length; i++) {

            Ingredient ingredient;
            if (ActionIdentifier.FREE_TEXT.equals(actionIdentifier) || ActionIdentifier.DIVIDER.equals(actionIdentifier)) {
                ingredient = Ingredient.builder()
                        .description(instructionString)
                        .unit(Unit.QUANTITY)
                        .quantity(0)
                        .build();
            } else {
                ingredient = parseIngredient(parts[i]);
            }

            ingredients[i] = ingredient;
        }

        return new Action(actionIdentifier, ingredients);
    }

    private static Ingredient parseIngredient(String value) {
        String[] parts = value.trim().split(" ");

        if (parts.length == 1) {
            return Ingredient.builder().description(parts[0]).quantity(1).unit(Unit.QUANTITY).build();
        }

        double quantity = Double.parseDouble(parts[0]);
        Unit unit;
        String identifier;
        if (parts.length == 3) {
            unit = Unit.parse(parts[1]);
            identifier = parts[2];
        } else {
            unit = Unit.QUANTITY;
            identifier = parts[1];
        }
        return Ingredient.builder().description(identifier).quantity(quantity).unit(unit).build();
    }
}
