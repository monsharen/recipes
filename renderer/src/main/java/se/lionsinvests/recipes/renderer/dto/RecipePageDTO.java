package se.lionsinvests.recipes.renderer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import se.lionsinvests.recipes.renderer.ActionTranslator;
import se.lionsinvests.recipes.renderer.UnitTranslator;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.ActionIdentifier;
import se.lionsinvests.recipes.sdk.Ingredient;
import se.lionsinvests.recipes.sdk.Recipe;

@AllArgsConstructor
@Builder
@Data
public class RecipePageDTO {

    private final Recipe recipe;
    private final UnitTranslator unitTranslator;
    private final ActionTranslator actionTranslator;
    private int actionCounter = 0;

    public String getAmountAndUnit(Ingredient ingredient) {
        if (ingredient == null) {
            return "";
        }
        String unit = unitTranslator.translate(ingredient.unit);
        return String.format("%.0f", ingredient.quantity) + " " + unit;
    }

    public int nextActionCount() {
        actionCounter += 1;
        return actionCounter;
    }

    public boolean isNumberedAction(Action action) {
        if (ActionIdentifier.IMAGE.equals(action.getActionIdentifier())) {
            return false;
        }

        if (ActionIdentifier.VIDEO.equals(action.getActionIdentifier())) {
            return false;
        }

        return true;
    }

    public String getActionDescription(Action action) {
        return actionTranslator.translate(action);
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
