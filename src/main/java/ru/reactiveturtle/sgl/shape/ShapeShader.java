package ru.reactiveturtle.sgl.shape;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import ru.reactiveturtle.sgl.Shader;

public class ShapeShader extends Shader {
    private int v_orthographicMatrixLocation;
    private int v_viewMatrixLocation;
    private int v_modelMatrixLocation;
    private int f_colorLocation;

    protected ShapeShader() {
        super("src/main/resources/shader/vertexShader.glsl",
                "src/main/resources/shader/fragmentShader.glsl");
        create();
    }

    @Override
    public void bindAllAttributes() {
        super.bindAttribute(0, "v_vertex");
    }

    @Override
    public void getAllUniforms() {
        v_orthographicMatrixLocation = getUniform("v_orthographicMatrix");
        v_viewMatrixLocation = getUniform("v_viewMatrix");
        v_modelMatrixLocation = getUniform("v_modelMatrix");
        f_colorLocation = getUniform("f_color");
    }

    @Override
    public void load(Object data) {
        ShapeShaderLoadData shaderData = (ShapeShaderLoadData) data;
        loadMatrix4fUniform(v_orthographicMatrixLocation, shaderData.getOrthographicMatrix());
        loadMatrix4fUniform(v_viewMatrixLocation, shaderData.getViewMatrix());
        loadModelMatrix(shaderData.getModelMatrix());
    }

    public void loadModelMatrix(Matrix4f modelMatrix) {
        loadMatrix4fUniform(v_modelMatrixLocation, modelMatrix);
    }

    public void loadColor(Vector4f colorVector) {
        loadVector4fUniform(f_colorLocation, colorVector);
    }
}
