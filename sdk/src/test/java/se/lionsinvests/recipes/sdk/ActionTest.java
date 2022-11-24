package se.lionsinvests.recipes.sdk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionTest {

    @Test
    public void shouldParseFourPartAction() {
        Action action = new Action("boil 2 l water");
        assertEquals(ActionIdentifier.BOIL, action.getActionIdentifier());
        assertEquals(Unit.LITRE, action.getIngredients()[0].unit);
        assertEquals(2, action.getIngredients()[0].getQuantity());
        assertEquals("water", action.getIngredients()[0].getDescription());
    }

    @Test
    public void shouldParseBoilPasta() {
        Action action = new Action("boil 400 g pasta-strozzapreti > 400 g pasta-strozzapreti");
        assertEquals(ActionIdentifier.BOIL, action.getActionIdentifier());
        assertEquals(Unit.GRAM, action.getIngredients()[0].unit);
        assertEquals(400, action.getIngredients()[0].getQuantity());
        assertEquals("pasta-strozzapreti", action.getIngredients()[0].getDescription());
    }

    @Test
    public void shouldParseActionWithoutUnit() {
        Action action = new Action("chop 1 garlic");
        assertEquals(ActionIdentifier.CHOP, action.getActionIdentifier());
        assertEquals(Unit.QUANTITY, action.getIngredients()[0].unit);
        assertEquals(1, action.getIngredients()[0].getQuantity());
        assertEquals("garlic", action.getIngredients()[0].getDescription());
    }

    @Test
    public void shouldHandleCombineAction() {
        Action action = new Action("combine pasta tomato garlic");
        assertEquals(ActionIdentifier.COMBINE, action.getActionIdentifier());
        assertEquals(3, action.getIngredients().length);
        assertEquals("pasta", action.getIngredients()[0].getDescription());
        assertEquals("tomato", action.getIngredients()[1].getDescription());
        assertEquals("garlic", action.getIngredients()[2].getDescription());
    }

}
