package se.lionsinvests.recipes.renderer;

public interface PageRenderer<P> {

    String render(P recipe);
}
