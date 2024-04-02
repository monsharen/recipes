package se.lionsinvests.recipes.renderer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.lionsinvests.recipes.renderer.translation.FreeTextActionTranslator;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.ActionIdentifier;
import se.lionsinvests.recipes.sdk.Ingredient;
import se.lionsinvests.recipes.sdk.unitconversion.SwedishUnitTranslator;
import se.lionsinvests.recipes.sdk.unitconversion.UnitTranslator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FreeTextActionTranslatorTest {

    private FreeTextActionTranslator instance;

    @BeforeEach
    public void before() {
        UnitTranslator unitTranslator = new SwedishUnitTranslator();
        List<String> supportedIngredients = new ArrayList<>();
        supportedIngredients.add("vatten");
        UnitHtmlRenderer unitHtmlRenderer = new UnitHtmlRenderer(unitTranslator, supportedIngredients);
        instance = new FreeTextActionTranslator(unitHtmlRenderer);
    }

    @Test
    public void testTranslate() {
        Action action = Action.builder()
                .actionIdentifier(ActionIdentifier.FREE_TEXT)
                .ingredients(new Ingredient[] {
                        Ingredient.builder().description(
                                "Sätt ugnen på 200 grader").build()
                })
                .build();
        String actualText = instance.translate(action);
        String expectedText = "Sätt ugnen på <span class=\"temperature\">200 grader</span>";
        assertEquals(expectedText, actualText);

        action = Action.builder()
                .actionIdentifier(ActionIdentifier.FREE_TEXT)
                .ingredients(new Ingredient[] {
                        Ingredient.builder().description(
                                "Sätt ugnen på 200 grader i cirka 12 minuter.").build()
                })
                .build();

        actualText = instance.translate(action);
        expectedText = "Sätt ugnen på <span class=\"temperature\">200 grader</span> i cirka <span class=\"time\">12 minuter</span>.";
        assertEquals(expectedText, actualText);
    }
}
