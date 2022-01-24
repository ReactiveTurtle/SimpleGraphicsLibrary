package ru.reactiveturtle.sgl.shape;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import ru.reactiveturtle.sgl.Color;
import ru.reactiveturtle.sgl.utils.MatrixExtensions;

public class Circle extends Shape {
    private static final float DRAW_ITERATION_RADIANS = (float) (Math.PI / (180 * 10));
    private static final int DRAW_ITERATION_COUNT = (int) (Math.PI / DRAW_ITERATION_RADIANS) * 4;
    private float radius;

    public Circle(float radius, float strokeWidth) {
        super(
                getFillVertices(radius),
                getFillIndices(),
                getStrokeVertices(radius, strokeWidth),
                getStrokeIndices());
        this.radius = radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        setFillVertices(getFillVertices(radius), getFillIndices());
        setStrokeVertices(getStrokeVertices(radius, getStrokeWidth()), getStrokeIndices());
    }

    @Override
    public void setStrokeWidth(float strokeWidth) {
        super.setStrokeWidth(strokeWidth);
        setStrokeVertices(getStrokeVertices(radius, strokeWidth), getStrokeIndices());
    }

    @Override
    public void draw(ShapeShader shapeShader) {
        GL30.glBindVertexArray(getFillVertexArrayId());
        GL20.glEnableVertexAttribArray(0);
        shapeShader.loadColor(new Color(getFillColor()).getComponentsAsVector());
        GL20.glDrawElements(GL11.GL_TRIANGLES, getFillVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        float startRotation = getRotationRadians();
        for (int i = 1; i < DRAW_ITERATION_COUNT; i++) {
            setRotationRadians(i * DRAW_ITERATION_RADIANS / 2f);
            shapeShader.loadModelMatrix(MatrixExtensions.getModelMatrix(this));
            GL20.glDrawElements(GL11.GL_TRIANGLES, getFillVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        }
        setRotationRadians(startRotation);
        GL20.glDisableVertexAttribArray(getFillVertexArrayId());

        GL30.glBindVertexArray(getStrokeVertexArrayId());
        GL20.glEnableVertexAttribArray(0);
        shapeShader.loadColor(new Color(getStrokeColor()).getComponentsAsVector());
        GL20.glDrawElements(GL11.GL_TRIANGLES, getStrokeVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        startRotation = getRotationRadians();
        for (int i = 1; i < DRAW_ITERATION_COUNT; i++) {
            setRotationRadians(i * DRAW_ITERATION_RADIANS / 2f);
            shapeShader.loadModelMatrix(MatrixExtensions.getModelMatrix(this));
            GL20.glDrawElements(GL11.GL_TRIANGLES, getStrokeVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        }
        setRotationRadians(startRotation);
        GL20.glDisableVertexAttribArray(getStrokeVertexArrayId());
        GL30.glBindVertexArray(0);
    }

    private static float[] getFillVertices(float radius) {
        return new float[]{
                0,
                0,
                0,
                radius,
                (float) (Math.sin(DRAW_ITERATION_RADIANS) * radius),
                (float) (Math.cos(DRAW_ITERATION_RADIANS) * radius),
        };
    }

    private static int[] getFillIndices() {
        return new int[]{0, 1, 2};
    }

    private static float[] getStrokeVertices(
            float radius,
            float strokeWidth) {
        return new float[]{
                0, radius,
                0, radius + strokeWidth,
                (float) (Math.sin(DRAW_ITERATION_RADIANS) * radius),
                (float) (Math.cos(DRAW_ITERATION_RADIANS) * radius),
                (float) (Math.sin(DRAW_ITERATION_RADIANS) * (radius + strokeWidth)),
                (float) (Math.cos(DRAW_ITERATION_RADIANS) * (radius + strokeWidth)),
        };
    }

    private static int[] getStrokeIndices() {
        return new int[]{
                0, 1, 2,
                2, 1, 3
        };
    }
}
