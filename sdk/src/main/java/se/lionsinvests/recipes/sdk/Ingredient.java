package se.lionsinvests.recipes.sdk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.lionsinvests.recipes.sdk.unitconversion.UnitConverter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Ingredient {
    public double quantity;
    public Unit unit;
    public String description;

    public Ingredient(String data) {
        String[] parts = data.split(" ");

        this.quantity = Double.parseDouble(parts[0]);

        if (parts.length == 2) {
            this.unit = Unit.QUANTITY;
            this.description = parts[1];
        } else {
            this.unit = UnitConverter.parse(parts[1]);
            this.description = data.substring(parts[0].length() + 1 + parts[1].length() + 1);
        }
    }
}
