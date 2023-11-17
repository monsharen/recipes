package se.lionsinvests.recipes.renderer;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import se.lionsinvests.recipes.sdk.unitconversion.SwedishUnitTranslator;
import se.lionsinvests.recipes.sdk.unitconversion.UnitTranslator;

@Log
public class UnitConverterTest {

    @Test
    public void test() {
        UnitTranslator unitTranslator = new SwedishUnitTranslator();
        UnitHtmlRenderer unitConverter = new UnitHtmlRenderer(unitTranslator);
        String sentence = "Koka upp 2 st citroner, 3 krm salt, 1 dl vatten, 3msk vinäger och 1 schalottenlök. Reducera till ca 1 msk återstår. 10x10 cm";
        String result = unitConverter.reformatQuantitiesAndUnits(sentence);

        log.info(sentence);
        log.info(result);
    }
}
