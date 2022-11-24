package se.lionsinvests.recipes.renderer;

import htmlflow.HtmlView;
import lombok.AllArgsConstructor;
import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Html;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class HtmlFrontpageRenderer {

    private final HtmlTemplate htmlTemplate;
    private final List<RecipeLink> recipeLinks;

    public String render() {

        List<RecipeLink> sortedRecipeLinks = recipeLinks.stream().sorted().collect(Collectors.toList());

        Div<Body<Html<HtmlView<Object>>>> pageContent = htmlTemplate.getPageContent();

        pageContent.h1().text("All Recipes").__();

        for (RecipeLink recipeLink : sortedRecipeLinks) {
            pageContent
                    .div()
                        .p()
                            .img().attrClass("recipe-image-small").attrSrc(recipeLink.image).attrAlt(recipeLink.name).__()
                            .a().attrHref(recipeLink.url).text(recipeLink.name).__()
                        .__()
                    .__();
        }

        return htmlTemplate.render();
    }

    @AllArgsConstructor
    public static class RecipeLink implements Comparable<RecipeLink>{
        private final String url;
        private final String name;
        private final String image;

        @Override
        public int compareTo(RecipeLink o) {
            return this.name.compareTo(o.name);
        }
    }
}
