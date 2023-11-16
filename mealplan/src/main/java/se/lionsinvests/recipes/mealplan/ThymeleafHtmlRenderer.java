package se.lionsinvests.recipes.mealplan;

import lombok.AllArgsConstructor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import se.lionsinvests.recipes.mealplan.dto.EmailDTO;
import se.lionsinvests.recipes.mealplan.dto.RecipeDTO;

import java.io.File;
import java.io.StringWriter;
import java.util.List;

@AllArgsConstructor
public class ThymeleafHtmlRenderer {
    private final File thymeleafTemplate;

    public String render(List<RecipeDTO> recipes, List<String> ingredients) {
        EmailDTO emailDTO = EmailDTO.builder()
                .recipes(recipes)
                .ingredients(ingredients)
                .build();

        Context context = new Context();
        context.setVariable("data", emailDTO);
        StringWriter stringWriter = new StringWriter();
        TemplateEngine templateEngine = new TemplateEngine();
        ITemplateResolver templateResolver = new FileTemplateResolver();
        templateEngine.setTemplateResolver(templateResolver);

        templateEngine.process(thymeleafTemplate.getAbsolutePath(), context, stringWriter);
        return stringWriter.toString();
    }
}
