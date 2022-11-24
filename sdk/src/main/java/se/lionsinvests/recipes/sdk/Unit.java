package se.lionsinvests.recipes.sdk;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@Getter
@AllArgsConstructor
public enum Unit {
    DEGREES_CELSIUS("C", "°C"),
    TABLESPOON("tbsp", "tablespoon"),
    TEASPOON("tsp", "teaspoon"),
    LITRE("l", "litres"),
    MILLILITRE("ml", "millilitres"),
    DECILITRE("dl", "decilitres"),
    CENTILITRE("cl", "centilitres"),
    GRAM("g", "grams"),
    KILOGRAM("kg", "kilograms"),
    QUANTITY("u", "units");

    private final String unit;
    private final String displayName;

    public static Unit parse(String value) {
        String lowercaseValue = value.toLowerCase(Locale.ROOT);
        for (Unit u : Unit.values()) {
            if (u.unit.toLowerCase(Locale.ROOT).equals(lowercaseValue)) {
                return u;
            }
        }
        throw new IllegalStateException("unsupported unit '" + value + "'");
    }
}
