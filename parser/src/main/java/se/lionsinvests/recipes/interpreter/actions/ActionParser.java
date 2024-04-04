package se.lionsinvests.recipes.interpreter.actions;

import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.ActionIdentifier;

import java.util.Locale;
import java.util.Map;

public class ActionParser {

    private static final Map<ActionIdentifier, ActionParserInterface> ACTION_PARSERS = Map.of(
            ActionIdentifier.FREE_TEXT, new FreeTextParser(),
            ActionIdentifier.IMAGE, new ImageParser(),
            ActionIdentifier.YOUTUBE, new YoutubeParser(),
            ActionIdentifier.DIVIDER, new DividerParser()
    );

    public Action parse(String value) {
        String actionString = value.substring(0, value.indexOf(" ")).toUpperCase(Locale.ROOT);
        String instructionString = value.substring(actionString.length() + 1);

        ActionIdentifier actionIdentifier = ActionIdentifier.valueOf(actionString);

        ActionParserInterface actionParserInterface = ACTION_PARSERS.get(actionIdentifier);

        if (actionParserInterface != null) {
            return actionParserInterface.parse(instructionString);
        }

        throw new IllegalArgumentException("Unknown action identifier: " + actionString);
    }
}
