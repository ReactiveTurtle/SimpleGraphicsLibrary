package ru.reactiveturtle.sgl.shape;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import ru.reactiveturtle.sgl.Disposable;
import ru.reactiveturtle.sgl.Transform2D;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public abstract class Shape extends Transform2D implements ShapeImpl, Disposable {
    private final int fillVertexArrayId;
    private final int fillVertexBufferId;
    private final int fillIndicesBufferId;
    private int fillVertexCount;

    private final int strokeVertexArrayId;
    private final int strokeVertexBufferId;
    private final int strokeIndicesBufferId;
    private int strokeVertexCount;

    private int fillColor = 0;
    private float strokeWidth = 0;
    private int strokeColor = 0;

    public Shape(float[] fillVertices, int[] fillIndices,
                 float[] strokeVertices, int[] strokeIndices) {
        fillVertexArrayId = GL30.glGenVertexArrays();
        fillVertexBufferId = GL15.glGenBuffers();
        fillIndicesBufferId = GL15.glGenBuffers();
        setFillVertices(fillVertices, fillIndices);

        strokeVertexArrayId = GL30.glGenVertexArrays();
        strokeVertexBufferId = GL15.glGenBuffers();
        strokeIndicesBufferId = GL15.glGenBuffers();
        setStrokeVertices(strokeVertices, strokeIndices);
    }

    public void setFillVertices(float[] vertices, int[] indices) {
        GL30.glBindVertexArray(fillVertexArrayId);
        storeData(fillVertexBufferId, vertices, 0);
        storeIndices(fillIndicesBufferId, indices);
        fillVertexCount = indices.length;
        GL30.glBindVertexArray(0);
    }

    public void setStrokeVertices(float[] strokeVertices, int[] strokeIndices) {
        GL30.glBindVertexArray(strokeVertexArrayId);
        storeData(strokeVertexBufferId, strokeVertices, 0);
        storeIndices(strokeIndicesBufferId, strokeIndices);
        strokeVertexCount = strokeIndices.length;
        GL30.glBindVertexArray(0);
    }

    @Override
    public void setFillColor(int color) {
        fillColor = color;
    }

    @Override
    public int getFillColor() {
        return fillColor;
    }

    @Override
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    @Override
    public float getStrokeWidth() {
        return strokeWidth;
    }

    @Override
    public void setStrokeColor(int color) {
        this.strokeColor = color;
    }

    @Override
    public int getStrokeColor() {
        return strokeColor;
    }

    public int getFillVertexArrayId() {
        return fillVertexArrayId;
    }

    public int getStrokeVertexArrayId() {
        return strokeVertexArrayId;
    }

    public int getFillVertexCount() {
        return fillVertexCount;
    }

    public int getStrokeVertexCount() {
        return strokeVertexCount;
    }

    @Override
    public void dispose() {
        GL15.glDeleteBuffers(new int[]{
                fillVertexBufferId,
                fillIndicesBufferId,
                strokeVertexBufferId,
                strokeIndicesBufferId});
        GL30.glDeleteVertexArrays(new int[]{fillVertexArrayId, strokeVertexArrayId});
    }

    public abstract void draw(ShapeShader shapeShader);

    protected static int storeIndices(int indicesBufferId, int[] indices) {
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBufferId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return indicesBufferId;
    }

    protected static int storeData(int bufferId, float[] data, int index) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data).flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, 2, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return bufferId;
    }
}
