package se.lionsinvests.recipes.renderer;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import se.lionsinvests.recipes.renderer.dto.RecipeDTO;

@AllArgsConstructor
public class JsonRenderer {

    private final List<RecipeDTO> recipes;

    public String render() {
        try {
            return new ObjectMapper().writeValueAsString(recipes);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("failed to convert recipe list to json", e);
        }
    }


}
