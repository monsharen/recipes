package se.lionsinvests.recipes.sdk;

import lombok.*;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Metadata {
    private String name;
    private String description;
    private String notes;
    private String servings;
    private String estimatedPrepTime;
    private List<String> images;
}
