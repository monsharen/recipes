package se.lionsinvests.recipes.interpreter.actions;

import se.lionsinvests.recipes.sdk.Action;

public interface ActionParserInterface {

    Action parse(String instructionString);
}
