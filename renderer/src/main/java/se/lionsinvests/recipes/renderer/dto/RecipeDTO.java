package se.lionsinvests.recipes.renderer.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class RecipeDTO {

    private final String name;
    private final List<String> types;
    private final String image;
    private final String url;
    private final String description;
}
