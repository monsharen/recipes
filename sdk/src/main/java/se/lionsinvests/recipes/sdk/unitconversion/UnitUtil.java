package se.lionsinvests.recipes.sdk.unitconversion;

import java.util.Locale;

public class UnitUtil {

    /**
     * Locale used when formatting quantities for display. Pinned so the rendered pages
     * are identical whatever locale the build machine happens to run under.
     */
    public static final Locale DISPLAY_LOCALE = Locale.forLanguageTag("sv-SE");

    public static boolean hasMoreThanOneDecimal(double number) {
        // Multiply by 10 and cast to int to truncate after one decimal place
        int truncated = (int)(number * 10);

        // Divide by 10.0 to get back to the original scale
        double oneDecimalNumber = truncated / 10.0;

        // Compare with the original number
        return number != oneDecimalNumber;
    }

    public static boolean hasFractionalPart(double value) {
        return value != Math.floor(value);
    }
}
