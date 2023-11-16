package se.lionsinvests.recipes.mealplan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import se.lionsinvests.recipes.sdk.Recipe;

@Builder
@AllArgsConstructor
@Data
public class RecipeDTO {

    private final String url;
    private final Recipe recipe;
}
