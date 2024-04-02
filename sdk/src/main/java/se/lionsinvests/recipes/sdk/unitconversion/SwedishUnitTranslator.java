package se.lionsinvests.recipes.sdk.unitconversion;

import se.lionsinvests.recipes.sdk.Unit;

import java.util.HashMap;
import java.util.Map;

import static se.lionsinvests.recipes.sdk.Unit.*;
import static se.lionsinvests.recipes.sdk.unitconversion.UnitUtil.hasFractionalPart;

public class SwedishUnitTranslator implements UnitTranslator {

    private static final Map<Unit, String> UNIT_TRANSLATION_SINGULAR = new HashMap<>()
    {{
        //put(DEGREES_CELSIUS, "grad");
        put(TABLESPOON, "matsked");
        put(TEASPOON, "tesked");
        put(LITRE, "liter");
        put(MILLILITRE, "milliliter");
        put(DECILITRE, "deciliter");
        put(CENTILITRE, "centiliter");
        put(GRAM, "gram");
        put(KILOGRAM, "kilogram");
        put(CUPS, "kopp");
        put(PINCH, "kryddmått");
        put(QUANTITY, "st");
        put(PIECE, "bit");
        put(CLOVE, "klyfta");
        put(CAN, "burk");
        put(JAR, "burk");
        put(PACKET, "paket");
        put(DASH, "nypa");
        put(BOTTLE, "flaska");
    }};

    private static final Map<Unit, String> UNIT_TRANSLATION_PLURAL = new HashMap<>()
    {{
        //put(DEGREES_CELSIUS, "grader");
        put(TABLESPOON, "matskedar");
        put(TEASPOON, "teskedar");
        put(LITRE, "liter");
        put(MILLILITRE, "milliliter");
        put(DECILITRE, "deciliter");
        put(CENTILITRE, "centiliter");
        put(GRAM, "gram");
        put(KILOGRAM, "kilogram");
        put(CUPS, "koppar");
        put(PINCH, "kryddmått");
        put(QUANTITY, "st");
        put(PIECE, "bitar");
        put(CLOVE, "klyftor");
        put(CAN, "burkar");
        put(JAR, "burkar");
        put(PACKET, "paket");
        put(DASH, "droppar");
        put(BOTTLE, "flaskor");
    }};

    @Override
    public String getUnitDisplayName(Unit unit, double amount) {
        String displayName = getTranslatedUnitName(unit, amount);

        if (amount == 0) {
            return displayName;
        }

        String displayAmount;

        if (hasFractionalPart(amount)) {
            displayAmount = String.format("%.1f", amount);
        } else {
            displayAmount = String.format("%.0f", amount);
        }

        return displayAmount + " " + displayName;
    }

    private String getTranslatedUnitName(Unit unit, double quantity) {
        String translation;

        if (quantity < 2) {
            translation = UNIT_TRANSLATION_SINGULAR.get(unit);
        } else {
            translation = UNIT_TRANSLATION_PLURAL.get(unit);
        }

        if (translation != null) {
            return translation;
        }

        return unit.getDisplayName();
    }


}
