package se.lionsinvests.recipes.mealplan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class EmailDTO {

    private final List<RecipeDTO> recipes;
    private final List<String> ingredients;

    public String getPresentationImage(RecipeDTO recipe) {
        if (recipe.getRecipe().getMetadata().getImages() == null || recipe.getRecipe().getMetadata().getImages().size() == 0) {
            return "https://i.imgur.com/u37OTv1.png";
        }

        return recipe.getRecipe().getMetadata().getImages().get(0);
    }

    public String getRecipeUrl(RecipeDTO recipeDTO) {
        return "https://monsharen.gitlab.io/recipes/" + recipeDTO.getUrl();
    }
}
