package se.lionsinvests.recipes.sdk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Action {

    private ActionIdentifier actionIdentifier;
    private Ingredient[] ingredients;

    private String description;

    public Action(String value) {
        Action action = ActionParser.parse(value);
        this.actionIdentifier = action.getActionIdentifier();
        this.ingredients = action.ingredients;
        this.description = action.description;
    }
}
