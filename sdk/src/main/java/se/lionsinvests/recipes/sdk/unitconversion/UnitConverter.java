package se.lionsinvests.recipes.sdk.unitconversion;

import lombok.AllArgsConstructor;
import se.lionsinvests.recipes.sdk.Unit;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static se.lionsinvests.recipes.sdk.Unit.*;

@AllArgsConstructor
public class UnitConverter {

    private static final Map<String, Unit> UNIT_KEYWORDS = new HashMap<>()
    {{
        // English
        put("c", DEGREES_CELSIUS);
        put("tbsp", TABLESPOON);
        put("tsp", TEASPOON);
        put("l", Unit.LITRE);
        put("ml", Unit.MILLILITRE);
        put("dl", Unit.DECILITRE);
        put("cl", Unit.CENTILITRE);
        put("g", Unit.GRAM);
        put("kg", Unit.KILOGRAM);
        put("u", Unit.QUANTITY);
        put("cups", Unit.CUPS);

        put("pinch", Unit.PINCH);
        put("pinches", Unit.PINCH);
        put("piece", Unit.PIECE);
        put("pieces", Unit.PIECE);
        put("clove", Unit.CLOVE);
        put("cloves", Unit.CLOVE);
        put("can", Unit.CAN);
        put("cans", Unit.CAN);
        put("jar", Unit.JAR);
        put("jars", Unit.JAR);
        put("packet", Unit.PACKET);
        put("packets", Unit.PACKET);
        put("dash", Unit.DASH);
        put("dashes", Unit.DASH);
        put("bottle", Unit.BOTTLE);

        // Swedish
        put("msk", TABLESPOON);
        put("tsk", TEASPOON);
        put("krm", Unit.PINCH);
        put("st", Unit.PIECE);
        put("klyfta", Unit.CLOVE);
        put("klyftor", Unit.CLOVE);
        put("burk", Unit.CAN);
        put("paket", Unit.PACKET);
        put("kopp", Unit.CUPS);
        put("nypa", Unit.DASH);
        put("nypor", Unit.DASH);
        put("flaska", Unit.BOTTLE);
    }};


    private static final Pattern QUANTITY_UNIT_PATTERN = Pattern.compile("(\\d+\\.?\\d*)\\s*(dl|msk)");

    private final UnitTranslator unitTranslator;

    public static Unit parse(String value) {
        String lowercaseValue = value.toLowerCase(Locale.ROOT);

        Unit unit = UNIT_KEYWORDS.get(lowercaseValue);

        if (unit != null) {
            return unit;
        }

        throw new IllegalStateException("unsupported unit '" + value + "'");
    }

    public String getUnitDisplayName(Unit unit, double quantity) {
        return unitTranslator.getUnitDisplayName(unit, quantity);
    }

    public String getUnitDisplayName(Unit unit) {
        return getUnitDisplayName(unit,0);
    }

    public String reformatQuantitiesAndUnits(String sentence) {
        Matcher matcher = QUANTITY_UNIT_PATTERN.matcher(sentence);
        StringBuilder reformattedSentence = new StringBuilder();

        while (matcher.find()) {
            String quantity = matcher.group(1);
            String unitString = matcher.group(2);

            Unit unit = UnitConverter.parse(unitString);

            double amount = Double.parseDouble(quantity);

            String unitDisplayName = unitTranslator.getUnitDisplayName(unit, amount);

            matcher.appendReplacement(reformattedSentence, unitDisplayName);
        }
        matcher.appendTail(reformattedSentence);

        return reformattedSentence.toString();
    }
}
