package se.lionsinvests.recipes.interpreter;

import org.junit.jupiter.api.Test;
import se.lionsinvests.recipes.sdk.Recipe;

import java.io.File;

public class InterpreterTest {

    @Test
    public void asd() throws Exception {
        Interpreter interpreter = new Interpreter();
        System.out.println(new File("../../resources/pasta_with_sicilian_pesto.recipe").getAbsolutePath());
        //Recipe recipe = interpreter.interpret(new File("../../resources/pasta_with_sicilian_pesto.recipe"));
        //System.out.println(recipe);
    }
}
