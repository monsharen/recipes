package se.lionsinvests.recipes.sdk;

import lombok.extern.java.Log;

import org.junit.jupiter.api.Test;
import se.lionsinvests.recipes.sdk.unitconversion.SwedishUnitTranslator;
import se.lionsinvests.recipes.sdk.unitconversion.UnitConverter;
import se.lionsinvests.recipes.sdk.unitconversion.UnitTranslator;

@Log
public class UnitConverterTest {

    @Test
    public void test() {
        UnitTranslator unitTranslator = new SwedishUnitTranslator();
        UnitConverter unitConverter = new UnitConverter(unitTranslator);
        String sentence = "Koka upp 1 dl vatten, 3 msk vinäger och 1 schalottenlök. Reducera till ca 1 msk återstår.";
        String result = unitConverter.reformatQuantitiesAndUnits(sentence);

        log.info(sentence);
        log.info(result);
    }
}
