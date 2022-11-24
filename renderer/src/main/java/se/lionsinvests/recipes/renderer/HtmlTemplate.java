package se.lionsinvests.recipes.renderer;

import htmlflow.HtmlView;
import htmlflow.StaticHtml;
import org.xmlet.htmlapifaster.*;

import java.lang.Object;

public class HtmlTemplate {

    private final String title;
    private final Html<HtmlView<Object>> html;
    private final Body<Html<HtmlView<Object>>> body;
    private final Div<Body<Html<HtmlView<Object>>>> pageContent;

    public HtmlTemplate(String title) {
        this.title = title;

        this.html = StaticHtml.view().html();
        init();
        this.body = html.body();
        this.pageContent = this.body.div().attrClass("content");
    }

    private void init() {
        html.attrLang("en");
        addHead(html.head());
    }

    public String render() {
        pageContent.__();
        body.__();
        return html.__().render();
    }

    public Div<Body<Html<HtmlView<Object>>>> getPageContent() {
        return pageContent;
    }

    private void addHead(Head<Html<HtmlView<Object>>> head) {
        head
                .meta().attrCharset("utf-8").__()
                .meta().attrName("viewport").attrContent("width=device-width, initial-scale=1").__()
                .link().attrHref("https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css").attrRel(EnumRelType.STYLESHEET).__()
                .link().attrHref("style.css").attrRel(EnumRelType.STYLESHEET).__()
                .title().text("Recipe - " + title).__()
                .__();
    }
}
