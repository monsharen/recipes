package se.lionsinvests.recipes.renderer;

import htmlflow.HtmlView;
import lombok.AllArgsConstructor;
import se.lionsinvests.recipes.renderer.dto.RecipeLinkDTO;

import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Html;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class HtmlFrontpageRenderer {

    private final HtmlTemplate htmlTemplate;
    private final List<RecipeLinkDTO> recipeLinks;

    public String render() {

        List<RecipeLinkDTO> sortedRecipeLinks = recipeLinks.stream().sorted().collect(Collectors.toList());

        Div<Body<Html<HtmlView<Object>>>> pageContent = htmlTemplate.getPageContent();

        pageContent.h1().text("All Recipes").__();

        for (RecipeLinkDTO recipeLink : sortedRecipeLinks) {
            pageContent
                    .div()
                        .p()
                            .img().attrClass("recipe-image-small").attrSrc(recipeLink.getImage()).attrAlt(recipeLink.getName()).__()
                            .a().attrHref(recipeLink.getUrl()).text(recipeLink.getName()).__()
                        .__()
                    .__();
        }

        return htmlTemplate.render();
    }


}
