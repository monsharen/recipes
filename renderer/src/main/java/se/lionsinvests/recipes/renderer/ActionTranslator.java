package se.lionsinvests.recipes.renderer;

import lombok.AllArgsConstructor;
import se.lionsinvests.recipes.renderer.translation.*;
import se.lionsinvests.recipes.sdk.Action;
import se.lionsinvests.recipes.sdk.ActionIdentifier;

@AllArgsConstructor
public class ActionTranslator implements Translator<Action> {

    private final UnitTranslator unitTranslator;

    @Override
    public String translate(Action action) {
        Translator<Action> translator = getTranslator(action.getActionIdentifier());
        return translator.translate(action);
    }

    private Translator<Action> getTranslator(ActionIdentifier actionIdentifier) {
        switch (actionIdentifier) {
            case FREE_TEXT:
                return new FreeTextActionTranslator();
            case SET:
                return new SetActionTranslator(unitTranslator);
            case BAKE:
                return new BakeActionTranslator(unitTranslator);
            case MIX:
                return new MixActionTranslator(unitTranslator);
            case COMBINE:
                return new CombineActionTranslator();
            case BOIL:
                return new BoilActionTranslator(unitTranslator);
            case CHOP:
                return new ChopActionTranslator();
            case ROAST:
                return new RoastActionTranslator(unitTranslator);
            case IMAGE:
                return new ImageActionTranslator();
            case YOUTUBE:
                return new YoutubeActionTranslator();
        }
        return obj -> "unsupported action";
    }
}
