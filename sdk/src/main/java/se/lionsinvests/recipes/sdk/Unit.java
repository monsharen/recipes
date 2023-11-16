package se.lionsinvests.recipes.sdk;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    CUPS("cups", "Cups"),

    PINCH("cups", "Cups"),
    QUANTITY("u", "units"),

    PIECE("piece", "piece"),
    CLOVE("clove", "Cloves"),
    CAN("can", "can" ),
    JAR("jar", "jar" ),
    PACKET("packet","packet" ),
    DASH("dash","dash" ),
    BOTTLE("bottle", "bottle");

    private final String unit;
    private final String displayName;
}
