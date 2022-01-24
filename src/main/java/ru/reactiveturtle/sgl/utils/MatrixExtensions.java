package ru.reactiveturtle.sgl.utils;

import org.joml.Matrix4f;
import ru.reactiveturtle.sgl.shape.ShapeImpl;

public final class MatrixExtensions {
    public static Matrix4f getModelMatrix(ShapeImpl shape) {
        return new Matrix4f()
                .translate(shape.getX(), shape.getY(), 0)
                .rotateZ(shape.getRotationRadians())
                .scale(shape.getScaleX(), shape.getScaleY(), 0);
    }
}
