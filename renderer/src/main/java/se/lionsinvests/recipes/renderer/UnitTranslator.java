package se.lionsinvests.recipes.renderer;

import se.lionsinvests.recipes.sdk.Unit;

public class UnitTranslator implements Translator<Unit> {
    @Override
    public String translate(Unit unit) {
        if (Unit.QUANTITY.equals(unit)) {
            return "";
        }
        return unit.getDisplayName();
    }
}
