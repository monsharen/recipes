package se.lionsinvests.recipes.renderer.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class FrontPageDTO {

    private List<String> categories;

    public List<String> getCategories() {
        return categories;
    }

}
