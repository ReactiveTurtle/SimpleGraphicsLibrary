package ru.reactiveturtle.sgl.shape;

import ru.reactiveturtle.sgl.*;
import ru.reactiveturtle.sgl.utils.MatrixExtensions;

import java.util.Objects;

public class ShapeBatch implements Disposable {
    private ShapeShader shapeShader;
    private final RenderContext renderContext;

    public ShapeBatch(RenderContext renderContext) {
        Objects.requireNonNull(renderContext);
        this.renderContext = renderContext;
        shapeShader = new ShapeShader();
    }

    public void begin() {
        shapeShader.bind();
    }

    public void draw(Shape shape) {
        ShapeShaderLoadData data = new ShapeShaderLoadData(
                renderContext.getCamera().getProjectionMatrix(),
                renderContext.getCamera().getViewMatrix(),
                MatrixExtensions.getModelMatrix(shape));
        shapeShader.load(data);
        shape.draw(shapeShader);
    }

    public void end() {
        shapeShader.unbind();
    }

    @Override
    public void dispose() {
        shapeShader.dispose();
    }
}
