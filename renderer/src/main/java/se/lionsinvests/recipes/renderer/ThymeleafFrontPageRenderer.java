package se.lionsinvests.recipes.renderer;

import java.io.File;
import java.io.StringWriter;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import lombok.AllArgsConstructor;
import se.lionsinvests.recipes.renderer.dto.RecipePageDTO;
import se.lionsinvests.recipes.sdk.Recipe;
import se.lionsinvests.recipes.sdk.unitconversion.UnitConverter;

@AllArgsConstructor
public class ThymeleafFrontPageRenderer implements PageRenderer<Recipe> {

    private final ActionTranslator actionTranslator;
    private final UnitConverter unitConverter;
    private final File thymeleafTemplate;

    @Override
    public String render(Recipe recipe) {
        RecipePageDTO recipePageDTO = RecipePageDTO.builder()
                .recipe(recipe)
                .unitConverter(unitConverter)
                .actionTranslator(actionTranslator)
                .build();

        Context context = new Context();
        context.setVariable("data", recipePageDTO);
        StringWriter stringWriter = new StringWriter();
        TemplateEngine templateEngine = new TemplateEngine();
        ITemplateResolver templateResolver = new FileTemplateResolver();
        templateEngine.setTemplateResolver(templateResolver);

        templateEngine.process(thymeleafTemplate.getAbsolutePath(), context, stringWriter);
        return stringWriter.toString();
    }
}
