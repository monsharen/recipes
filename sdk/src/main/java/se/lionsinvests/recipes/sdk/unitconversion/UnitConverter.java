package se.lionsinvests.recipes.sdk.unitconversion;

import lombok.extern.java.Log;
import se.lionsinvests.recipes.sdk.Unit;

import java.util.*;

import static se.lionsinvests.recipes.sdk.Unit.*;

@Log
public class UnitConverter {

    private static final Map<String, Unit> UNIT_KEYWORDS = new HashMap<>()
    {{
        // English
        //put("c", DEGREES_CELSIUS);
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
        put("st", QUANTITY);
        put("klyfta", Unit.CLOVE);
        put("klyftor", Unit.CLOVE);
        put("burk", Unit.CAN);
        put("paket", Unit.PACKET);
        put("kopp", Unit.CUPS);
        put("nypa", Unit.DASH);
        put("nypor", Unit.DASH);
        put("flaska", Unit.BOTTLE);
        put("klick", TABLESPOON);
    }};

    private final UnitTranslator unitTranslator;

    public UnitConverter(UnitTranslator unitTranslator) {
        this.unitTranslator = unitTranslator;
    }

    public static Set<String> getAllUnits() {
        return UNIT_KEYWORDS.keySet();
    }

    public static Set<String> getAllUnitsMinusTemperature() {
        Set<String> allUnits = getAllUnits();
        //allUnits.remove(DEGREES_CELSIUS.getUnit());
        return allUnits;
    }

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


}
