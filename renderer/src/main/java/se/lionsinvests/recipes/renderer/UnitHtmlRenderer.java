package se.lionsinvests.recipes.renderer;

import lombok.extern.java.Log;
import se.lionsinvests.recipes.sdk.Unit;
import se.lionsinvests.recipes.sdk.unitconversion.UnitConverter;
import se.lionsinvests.recipes.sdk.unitconversion.UnitTranslator;
import se.lionsinvests.recipes.sdk.unitconversion.WeightUnitConverter;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static se.lionsinvests.recipes.sdk.unitconversion.UnitConverter.getAllUnitsMinusTemperature;

@Log
public class UnitHtmlRenderer {


    private final UnitTranslator unitTranslator;

    private final Pattern quantityUnitPattern;
    private final Pattern timePattern;

    private final Pattern temperaturePattern;

    public UnitHtmlRenderer(UnitTranslator unitTranslator, List<String> supportedIngredients) {
        this.unitTranslator = unitTranslator;
        String regex = getQuantityUnitRegularExpression(supportedIngredients);
        log.info(regex);
        quantityUnitPattern = Pattern.compile(regex);
        timePattern = Pattern.compile("\\b\\d+\\s+(s|minuter|minutes|min|h|timme|timmar|hours|hrs|sekunder|seconds|sec)(?=\\s|\\.|,|;|!|\\?|\\n)");
        temperaturePattern = Pattern.compile("\\b\\d+\\s*°?\\s*(C(?!\\w)|Celsius|F(?!\\w)|Fahrenheit|grader)\\b(?=\\s|[.,;!?]|\\n|$)");
    }

    public String reformatQuantitiesAndUnits(String sentence) {
        Matcher matcher = quantityUnitPattern.matcher(sentence);
        StringBuilder reformattedSentence = new StringBuilder();

        while (matcher.find()) {
            String quantity = matcher.group(1);
            String unitString = matcher.group(2);
            String ingredientId = matcher.group(3);
            String punctuation = matcher.group(4) != null ? matcher.group(4) : "";

            Unit unit = UnitConverter.parse(unitString);

            double amount = Double.parseDouble(quantity);

            String weightAndVolume = getWeightAndVolume(ingredientId, unit, amount);

            String unitDisplayName = "<span class=\"quantity\">" + unitTranslator.getUnitDisplayName(unit, amount) + weightAndVolume + "</span>" + (ingredientId != null ? " " + ingredientId : "") + punctuation;

            matcher.appendReplacement(reformattedSentence, unitDisplayName);
        }
        matcher.appendTail(reformattedSentence);

        return reformattedSentence.toString();
    }

    private String getWeightAndVolume(String ingredientId, Unit unit, double amount) {
        String volume = WeightUnitConverter.getVolume(amount, unit);
        String weight = "";

        if (Unit.GRAM != unit && Unit.KILOGRAM != unit) {
            weight = WeightUnitConverter.getWeightInGram(ingredientId, unit, amount);
        }

        if (volume.isBlank() && weight.isBlank()) {
            return "";
        }

        String result = " (";
        if (!volume.isEmpty()) {
            result += volume;

            if (!weight.isEmpty()) {
                result += ", ";
            }
        }

        if (!weight.isEmpty()) {
            result += weight;
        }

        result += ")";

        return result;
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

    private String getQuantityUnitRegularExpression(List<String> supportedIngredients) {
        String numberPattern = "(\\d+[,.]?\\d*)";
        //.map(Pattern::quote) // Quote each key to handle special regex characters

        String unitPattern = String.join("|", getAllUnitsMinusTemperature());

        String ingredientPattern = String.join("|", supportedIngredients);

        // Make the ingredient pattern optional and non-capturing for the group itself, but capturing for the match
        // This pattern assumes an ingredient might follow the unit directly or be separated by whitespace
        return numberPattern + "\\s*(" + unitPattern + ")" +
                "(?!\\w)(?:\\s+(" + ingredientPattern + "))?" +
                "([,\\.]?\\s*|$)";
    }
}
