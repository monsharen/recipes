package se.lionsinvests.recipes.renderer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import se.lionsinvests.recipes.renderer.ActionTranslator;
import se.lionsinvests.recipes.sdk.unitconversion.UnitConverter;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.ActionIdentifier;
import se.lionsinvests.recipes.sdk.Ingredient;
import se.lionsinvests.recipes.sdk.Recipe;

@AllArgsConstructor
@Builder
@Data
public class RecipePageDTO {

    private final Recipe recipe;
    private final UnitConverter unitConverter;
    private final ActionTranslator actionTranslator;
    private int actionCounter = 0;

    public String getIngredientListAmountAndUnit(Ingredient ingredient) {
        if (ingredient == null) {
            return "";
        }
        return unitConverter.getUnitDisplayName(ingredient.unit, ingredient.quantity);
    }

    public int nextActionCount() {
        actionCounter += 1;
        return actionCounter;
    }

    public boolean isNumberedAction(Action action) {
        if (
                ActionIdentifier.IMAGE.equals(action.getActionIdentifier()) ||
                ActionIdentifier.YOUTUBE.equals(action.getActionIdentifier()) ||
                ActionIdentifier.DIVIDER.equals(action.getActionIdentifier())) {
            return false;
        }

        return true;
    }

    public String getActionClass(Action action) {

        if (
                ActionIdentifier.IMAGE.equals(action.getActionIdentifier()) ||
                        ActionIdentifier.YOUTUBE.equals(action.getActionIdentifier()) ||
                        ActionIdentifier.DIVIDER.equals(action.getActionIdentifier())) {
            return "empty_action";
        }

        if (ActionIdentifier.SET.equals(action.getActionIdentifier())) {
            return "action oven";
        }

        return "action";
    }

    public String getActionDescription(Action action) {
        try {
            return actionTranslator.translate(recipe, action);
        } catch (Exception e) {
            throw new IllegalStateException("failed to translate action " + action + " (" + actionCounter + ") for recipe " + recipe);
        }
    }

    public boolean hasActionIcon(Action action) {
        if (ActionIdentifier.SET.equals(action.getActionIdentifier())) {
            return true;
        }

        return false;
    }

    public String getPresentationImage() {
        if (recipe.getMetadata().getImages() == null || recipe.getMetadata().getImages().size() == 0) {
            return "https://i.imgur.com/u37OTv1.png";
        }

        return recipe.getMetadata().getImages().get(0);
    }
}
