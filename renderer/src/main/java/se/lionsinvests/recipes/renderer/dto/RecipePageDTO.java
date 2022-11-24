package se.lionsinvests.recipes.renderer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import se.lionsinvests.recipes.renderer.ActionTranslator;
import se.lionsinvests.recipes.renderer.UnitTranslator;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.Ingredient;
import se.lionsinvests.recipes.sdk.Recipe;

@AllArgsConstructor
@Builder
@Data
public class RecipePageDTO {

    private final Recipe recipe;
    private final UnitTranslator unitTranslator;
    private final ActionTranslator actionTranslator;

    public String getAmountAndUnit(Ingredient ingredient) {
        if (ingredient == null) {
            return "";
        }
        String unit = unitTranslator.translate(ingredient.unit);
        return String.format("%.0f", ingredient.quantity) + " " + unit;
    }

    public String getActionDescription(Action action) {
        return actionTranslator.translate(action);
    }

    public String getPresentationImage() {
        if (recipe.getMetadata().getImages() == null || recipe.getMetadata().getImages().size() == 0) {
            return "https://i.imgur.com/u37OTv1.png";
        }

        return recipe.getMetadata().getImages().get(0);
    }
}
