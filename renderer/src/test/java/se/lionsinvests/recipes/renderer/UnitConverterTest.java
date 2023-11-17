package se.lionsinvests.recipes.renderer;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import se.lionsinvests.recipes.sdk.Unit;
import se.lionsinvests.recipes.sdk.unitconversion.SwedishUnitTranslator;
import se.lionsinvests.recipes.sdk.unitconversion.UnitTranslator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Log
public class UnitConverterTest {

    @Test
    public void testSentence() {
        UnitTranslator unitTranslator = new SwedishUnitTranslator();
        UnitHtmlRenderer unitConverter = new UnitHtmlRenderer(unitTranslator);
        String sentence = "Koka upp 2 st citroner, 0.5 krm sten, 3 krm salt, 1 dl vatten, 3msk vinäger och 1 schalottenlök. Reducera till ca 1 msk återstår. 10x10 cm";
        String actual = unitConverter.reformatQuantitiesAndUnits(sentence);

        String expected = "Koka upp " +
                "<span class=\"quantity\">2 st</span>citroner, " +
                "<span class=\"quantity\">0,5 kryddmått (0,15 ml)</span>sten, " +
                "<span class=\"quantity\">3 kryddmått (0,9 ml)</span>salt, " +
                "<span class=\"quantity\">1 deciliter (100 ml)</span>vatten, " +
                "<span class=\"quantity\">3 matskedar (45 ml)</span>vinäger " +
                "och 1 schalottenlök. Reducera till ca " +
                "<span class=\"quantity\">1 matsked (15 ml)</span>återstår. 10x10 cm";
        assertEquals(expected, actual);
        log.info(sentence);
        log.info(actual);
    }

    @Test
    public void testDecimals() {
        boolean result = SwedishUnitTranslator.hasMoreThanOneDecimal(0.9);
        assertFalse(result);

        result = SwedishUnitTranslator.hasMoreThanOneDecimal(0);
        assertFalse(result);

        result = SwedishUnitTranslator.hasMoreThanOneDecimal(0.91);
        Assertions.assertTrue(result);
    }

    @Test
    public void testUnits() {
        String result = SwedishUnitTranslator.getUnitInMl(0.5, Unit.PINCH);
        assertEquals(" (0,15 ml)", result);

        result = SwedishUnitTranslator.getUnitInMl(3, Unit.PINCH);
        assertEquals(" (0,9 ml)", result);
    }

    @Test
    public void testTime() {
        UnitTranslator unitTranslator = new SwedishUnitTranslator();
        UnitHtmlRenderer unitConverter = new UnitHtmlRenderer(unitTranslator);

        String actual = unitConverter.reformatTimes("20 min.");
        assertEquals("<span class=\"time\">20 min</span>.", actual);

        actual = unitConverter.reformatTimes("20 minutes.");
        assertEquals("<span class=\"time\">20 minutes</span>.", actual);

        actual = unitConverter.reformatTimes("20 minuter ");
        assertEquals("<span class=\"time\">20 minuter</span> ", actual);
    }

    @Test
    public void testTemperature() {
        UnitTranslator unitTranslator = new SwedishUnitTranslator();
        UnitHtmlRenderer unitConverter = new UnitHtmlRenderer(unitTranslator);

        String actual = unitConverter.reformatTemperature("200 C.");
        assertEquals("<span class=\"temperature\">200 C</span>.", actual);

        actual = unitConverter.reformatTemperature("200 Celsius.");
        assertEquals("<span class=\"temperature\">200 Celsius</span>.", actual);

        actual = unitConverter.reformatTemperature("200 F ");
        assertEquals("<span class=\"temperature\">200 F</span> ", actual);
    }
}
