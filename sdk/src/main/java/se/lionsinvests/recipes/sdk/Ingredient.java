package se.lionsinvests.recipes.sdk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class Ingredient {
    public double quantity;
    public Unit unit;
    public String description;

}
