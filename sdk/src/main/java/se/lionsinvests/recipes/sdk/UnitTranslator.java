package se.lionsinvests.recipes.sdk;

import se.lionsinvests.recipes.sdk.Unit;

public class UnitTranslator {

    public String translate(Unit unit) {
        if (Unit.QUANTITY.equals(unit)) {
            return "";
        }
        return unit.getDisplayName();
    }
}
