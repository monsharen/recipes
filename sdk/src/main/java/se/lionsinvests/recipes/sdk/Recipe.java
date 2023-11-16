package se.lionsinvests.recipes.sdk;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class Recipe {
    private Metadata metadata;
    private List<Ingredient> ingredients;
    private List<Action> actions;
}
