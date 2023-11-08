package se.lionsinvests.recipes.renderer;

import org.junit.Before;
import org.junit.Test;
import se.lionsinvests.recipes.sdk.*;

import static org.junit.Assert.assertEquals;

public class ActionTranslatorTest {

    private UnitTranslator unitTranslator = new UnitTranslator();
    private ActionTranslator instance;

    @Before
    public void before() {
        instance = new ActionTranslator(unitTranslator);
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
        String actualText = instance.translate(action);
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
        String actualText = instance.translate(action);
        String expectedText = "Boil 200.0 grams of pasta";
        assertEquals(expectedText, actualText);
    }
}
