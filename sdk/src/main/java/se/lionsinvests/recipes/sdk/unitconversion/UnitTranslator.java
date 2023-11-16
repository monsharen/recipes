package se.lionsinvests.recipes.sdk.unitconversion;

import se.lionsinvests.recipes.sdk.Unit;

public interface UnitTranslator {

    String getUnitDisplayName(Unit unit, double amount);
}
