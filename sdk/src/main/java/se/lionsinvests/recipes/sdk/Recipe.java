package se.lionsinvests.recipes.sdk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Recipe {
    private Metadata metadata;
    private List<Ingredient> ingredients;
    private List<Action> actions;
}
