package se.lionsinvests.recipes.interpreter;

import se.lionsinvests.recipes.sdk.Ingredient;
import se.lionsinvests.recipes.sdk.Unit;
import se.lionsinvests.recipes.sdk.unitconversion.UnitConverter;

public class IngredientParser {

    public Ingredient parse(String data) {
        String[] parts = data.split(" ");

        double quantity = Double.parseDouble(parts[0]);

        Unit unit;
        String description;
        if (parts.length == 2) {
            unit = Unit.QUANTITY;
            description = parts[1];
        } else {
            unit = UnitConverter.parse(parts[1]);
            description = data.substring(parts[0].length() + 1 + parts[1].length() + 1);
        }

        return new Ingredient(quantity, unit, description);
    }
}
