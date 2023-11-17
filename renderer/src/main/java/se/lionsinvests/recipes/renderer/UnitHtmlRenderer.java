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

    public UnitHtmlRenderer(UnitTranslator unitTranslator) {
        this.unitTranslator = unitTranslator;
        String regex = getQuantityUnitRegularExpression();
        quantityUnitPattern = Pattern.compile(regex);
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

    private String getQuantityUnitRegularExpression() {
        return "(\\d+\\.?\\d*)\\s*(" +
                UnitConverter.getAllUnits().stream()
                        .map(Pattern::quote) // Quote each key to handle special regex characters
                        .collect(Collectors.joining("|"))
                + ")(\\s|$)";
    }
}
