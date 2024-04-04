package se.lionsinvests.recipes.interpreter;

import lombok.*;
import se.lionsinvests.recipes.sdk.Metadata;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class RecipeFile {
    private Metadata metadata;
    private List<String> ingredients;
    private List<String> actions;
}
