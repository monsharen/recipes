package se.lionsinvests.recipes.renderer;

import org.junit.Before;
import org.junit.Test;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.ActionIdentifier;
import se.lionsinvests.recipes.sdk.Ingredient;
import se.lionsinvests.recipes.sdk.Unit;
import se.lionsinvests.recipes.sdk.unitconversion.SwedishUnitTranslator;
import se.lionsinvests.recipes.sdk.unitconversion.UnitConverter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ActionTranslatorTest {

    private ActionTranslator instance;

    @Before
    public void before() {
        SwedishUnitTranslator unitTranslator = new SwedishUnitTranslator();
        UnitConverter unitConverter = new UnitConverter(unitTranslator);
        List<String> supportedIngredients = new ArrayList<>();
        UnitHtmlRenderer unitHtmlRenderer = new UnitHtmlRenderer(unitTranslator, supportedIngredients);
        instance = new ActionTranslator(unitConverter, unitHtmlRenderer);
    }

    @Test
    public void shouldTranslateCombineAction() {
        Action action = Action.builder()
                .actionIdentifier(ActionIdentifier.COMBINE)
                .ingredients(new Ingredient[] {
                        Ingredient.builder().description("garlic").build(),
                        Ingredient.builder().description("tomato").build(),
                        Ingredient.builder().description("nuts").build()
                })
                .build();
        String actualText = instance.translate(null, action);
        String expectedText = "Combine garlic, tomato and nuts in a bowl";
        assertEquals(expectedText, actualText);
    }

    @Test
    public void shouldTranslateBoilAction() {
        Action action = Action.builder()
                .actionIdentifier(ActionIdentifier.BOIL)
                .ingredients(new Ingredient[] {
                        Ingredient.builder().description("pasta").quantity(200).unit(Unit.GRAM).build()
                })
                .build();
        String actualText = instance.translate(null, action);
        String expectedText = "Boil 200.0 gram of pasta";
        assertEquals(expectedText, actualText);
    }
}
