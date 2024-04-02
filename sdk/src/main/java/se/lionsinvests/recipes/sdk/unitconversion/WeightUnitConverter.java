package se.lionsinvests.recipes.sdk.unitconversion;

import se.lionsinvests.recipes.sdk.Unit;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static se.lionsinvests.recipes.sdk.Unit.*;
import static se.lionsinvests.recipes.sdk.unitconversion.UnitUtil.hasFractionalPart;
import static se.lionsinvests.recipes.sdk.unitconversion.UnitUtil.hasMoreThanOneDecimal;

public class WeightUnitConverter {

    static final Map<Unit, Double> UNIT_ML_CONVERSION = new HashMap<>() {{
        put(TABLESPOON, 15.0);
        put(TEASPOON, 5.0);
        put(LITRE, 1000.0);
        put(DECILITRE, 100.0);
        put(CENTILITRE, 10.0);
        put(CUPS, 250.0);
        put(PINCH, 0.3);
    }};

    public static final Map<String, Double> INGREDIENT_DECILITER_WEIGHT_CONVERSION = new HashMap<>() {{
        put("kallt vatten", 100.0);
        put("vatten", 100.0);
        put("vinäger", 100.0);
        put("socker", 80.0);
        put("mjöl", 50.0);
        put("smör", 90.0);
    }};

    public static List<String> getSupportedIngredients() {
        return List.copyOf(INGREDIENT_DECILITER_WEIGHT_CONVERSION.keySet());
    }

    static final Map<Unit, Double> UNIT_TO_DECILITER_CONVERSION = new HashMap<>() {{
        put(TABLESPOON, 0.15);
        put(TEASPOON, 0.05);
        put(LITRE, 10.0);
        put(DECILITRE, 1.0);
        put(CENTILITRE, 0.1);
        put(CUPS, 2.5);
        put(PINCH, 0.003);
    }};

    public static String getVolume(double quantity, Unit unit) {
        Double ml = UNIT_ML_CONVERSION.get(unit);

        if (ml != null) {
            double result = Math.round(quantity * ml * 100.0) / 100.0;

            if (hasFractionalPart(result)) {

                if (hasMoreThanOneDecimal(result)) {
                    return String.format("%.2f ml", result);
                }

                return String.format("%.1f ml", result);
            }

            return String.format("%.0f ml", result);
        }

        return "";
    }

    public static String getWeightInGram(String ingredientId, Unit unit, double amount) {

        if (ingredientId == null) {
            return "";
        }

        Double unitMultiplier = UNIT_TO_DECILITER_CONVERSION.get(unit);

        if (unitMultiplier == null) {
            unitMultiplier = 1.0;
        }

        Double deciliterMultiplier = INGREDIENT_DECILITER_WEIGHT_CONVERSION.get(ingredientId.toLowerCase());

        if (deciliterMultiplier == null) {
            return "";
        }

        double result = Math.round(amount * deciliterMultiplier * unitMultiplier);

        if (hasFractionalPart(result)) {

            if (hasMoreThanOneDecimal(result)) {
                return String.format(Locale.ENGLISH, "%.2f g", result);
            }

            return String.format(Locale.ENGLISH, "%.1f g", result);
        }

        return String.format(Locale.ENGLISH, "%.0f g", result);
    }


}
