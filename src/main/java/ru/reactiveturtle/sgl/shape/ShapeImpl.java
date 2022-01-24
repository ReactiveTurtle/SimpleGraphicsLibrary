package ru.reactiveturtle.sgl.shape;

import ru.reactiveturtle.sgl.Transform2DImpl;

public interface ShapeImpl extends Transform2DImpl {
    void setFillColor(int color);

    int getFillColor();

    void setStrokeWidth(float strokeWidth);

    float getStrokeWidth();

    void setStrokeColor(int color);

    int getStrokeColor();
}
