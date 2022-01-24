package ru.reactiveturtle.sgl.shape;

import ru.reactiveturtle.sgl.Color;
import ru.reactiveturtle.sgl.OrthographicCamera;
import ru.reactiveturtle.sgl.RenderContext;
import ru.reactiveturtle.sgl.Window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ShapeRenderContext extends RenderContext {
    private Color clearColor = new Color(0f, 0f, 0f, 1f);

    private final List<Shape> shapeList = new ArrayList<>();
    private ShapeBatch shapeBatch;

    public ShapeRenderContext(Window window) {
        super(window);
    }

    public void addShape(Shape shape) {
        shapeList.add(Objects.requireNonNull(shape));
    }

    public Shape removeShape(int index) {
        return shapeList.get(index);
    }

    public List<Shape> getShapeList() {
        return Collections.unmodifiableList(shapeList);
    }

    public void setClearColor(Color clearColor) {
        Objects.requireNonNull(clearColor);
        this.clearColor = clearColor;
    }

    @Override
    public void start() {
        setCamera(new OrthographicCamera(getWindow().getWidth(), getWindow().getHeight()));
        shapeBatch = new ShapeBatch(this);
    }

    @Override
    public void onChangeWindowSize(int width, int height) {
        getCamera().setWidth(width);
        getCamera().setHeight(height);
    }

    @Override
    public void render() {
        clearColor(clearColor);
        clearColorBufferBit();
        shapeBatch.begin();
        for (Shape shape : shapeList) {
            shapeBatch.draw(shape);
        }
        shapeBatch.end();
    }

    @Override
    public void dispose() {
        shapeBatch.dispose();
        for (Shape shape : shapeList) {
            shape.dispose();
        }
        shapeList.clear();
    }

    @Override
    public OrthographicCamera getCamera() {
        return (OrthographicCamera) super.getCamera();
    }
}
