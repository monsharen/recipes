package se.lionsinvests.recipes.renderer;

import se.lionsinvests.recipes.sdk.Unit;
import se.lionsinvests.recipes.sdk.unitconversion.UnitConverter;
import se.lionsinvests.recipes.sdk.unitconversion.UnitTranslator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UnitHtmlRenderer {

    private final UnitTranslator unitTranslator;

    private final Pattern quantityUnitPattern;
    private final Pattern timePattern;

    private final Pattern temperaturePattern;

    public UnitHtmlRenderer(UnitTranslator unitTranslator) {
        this.unitTranslator = unitTranslator;
        String regex = getQuantityUnitRegularExpression();
        quantityUnitPattern = Pattern.compile(regex);
        timePattern = Pattern.compile("\\b\\d+\\s+(s|minuter|minutes|min|h|timmar|hours|hrs|sekunder|seconds|sec)(?=\\s|\\.|,|;|!|\\?|\\n)");
        temperaturePattern = Pattern.compile("\\b\\d+\\s*°?\\s*(C|Celsius|F|Fahrenheit)\\b(?=\\s|\\.|,|;|!|\\?|\\n)");
    }

    public String reformatQuantitiesAndUnits(String sentence) {
        Matcher matcher = quantityUnitPattern.matcher(sentence);
        StringBuilder reformattedSentence = new StringBuilder();

        while (matcher.find()) {
            String quantity = matcher.group(1);
            String unitString = matcher.group(2);

            Unit unit = UnitConverter.parse(unitString);

            double amount = Double.parseDouble(quantity);

            String unitDisplayName = "<span class=\"quantity\">" + unitTranslator.getUnitDisplayName(unit, amount) + "</span>";

            matcher.appendReplacement(reformattedSentence, unitDisplayName);
        }
        matcher.appendTail(reformattedSentence);

        return reformattedSentence.toString();
    }

    public String reformatTimes(String sentence) {
        Matcher matcher = timePattern.matcher(sentence);
        StringBuilder reformattedSentence = new StringBuilder();

        while (matcher.find()) {
            String time = matcher.group(0);

            String unitDisplayName = "<span class=\"time\">" + time + "</span>";

            matcher.appendReplacement(reformattedSentence, unitDisplayName);
        }
        matcher.appendTail(reformattedSentence);

        return reformattedSentence.toString();
    }

    public String reformatTemperature(String sentence) {
        Matcher matcher = temperaturePattern.matcher(sentence);
        StringBuilder reformattedSentence = new StringBuilder();

        while (matcher.find()) {
            String temperature = matcher.group(0);

            // Recalculate Farenheit/C/etc

            String unitDisplayName = "<span class=\"temperature\">" + temperature + "</span>";

            matcher.appendReplacement(reformattedSentence, unitDisplayName);
        }
        matcher.appendTail(reformattedSentence);

        return reformattedSentence.toString();
    }

    private String getQuantityUnitRegularExpression() {

        String numberPattern = "(\\d+[,.]?\\d*)";

        return numberPattern + "\\s*(" +
                UnitConverter.getAllUnits().stream()
                        .map(Pattern::quote) // Quote each key to handle special regex characters
                        .collect(Collectors.joining("|"))
                + ")(\\s|$)";
    }
}
