package ru.reactiveturtle.sgl.shape;

import org.joml.Matrix4f;

public class ShapeShaderLoadData {
    private final Matrix4f orthographicMatrix;
    private final Matrix4f viewMatrix;
    private final Matrix4f modelMatrix;

    public ShapeShaderLoadData(Matrix4f orthographicMatrix,
                               Matrix4f viewMatrix,
                               Matrix4f modelMatrix) {
        this.orthographicMatrix = new Matrix4f(orthographicMatrix);
        this.viewMatrix = new Matrix4f(viewMatrix);
        this.modelMatrix = new Matrix4f(modelMatrix);
    }

    public Matrix4f getOrthographicMatrix() {
        return new Matrix4f(orthographicMatrix);
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f(viewMatrix);
    }

    public Matrix4f getModelMatrix() {
        return new Matrix4f(modelMatrix);
    }
}
