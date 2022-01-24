package ru.reactiveturtle.sgl;

public interface WindowImpl {
    void show();

    void fullscreen();

    void setRenderContext(RenderContext renderContext);

    RenderContext getRenderContext();
}
