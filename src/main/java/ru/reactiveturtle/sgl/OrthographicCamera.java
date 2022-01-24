package ru.reactiveturtle.sgl;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class OrthographicCamera extends Transform2D implements CameraImpl {
    private float width;
    private float height;

    private final Matrix4f viewMatrix = new Matrix4f().identity();
    private final Matrix4f projectionMatrix = new Matrix4f().identity();

    public OrthographicCamera(float width, float height) {
        this.width = width;
        this.height = height;
        projectionMatrix.ortho2D(
                -width / 2f,
                width / 2f,
                -height / 2f,
                height / 2f);
    }

    public void setWidth(float width) {
        this.width = width;
        projectionMatrix.identity().ortho2D(
                -width / 2f,
                width / 2f,
                -height / 2f,
                height / 2f);
    }

    public void setHeight(float height) {
        this.height = height;
        projectionMatrix.identity().ortho2D(
                -width / 2f,
                width / 2f,
                -height / 2f,
                height / 2f);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Vector2f getCursorPosition(Window window, Vector2f cursorPosition) {
        Vector2f cursorCameraPosition = new Vector2f();
        cursorCameraPosition.x = (cursorPosition.x / window.getWidth() - 0.5f) * getWidth();
        cursorCameraPosition.y = (1 - cursorPosition.y / window.getHeight() - 0.5f) * getHeight();
        return cursorCameraPosition;
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        viewMatrix.identity()
                .rotateZ(getRotationRadians())
                .translate(-getX(), -getY(), 0)
                .scale(getScaleX(), getScaleY(), 0);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        viewMatrix.identity()
                .rotateZ(getRotationRadians())
                .translate(-getX(), -getY(), 0)
                .scale(getScaleX(), getScaleY(), 0);
    }

    @Override
    public void setRotationRadians(float rotationRadians) {
        super.setRotationRadians(rotationRadians);
        viewMatrix.identity()
                .rotateZ(getRotationRadians())
                .translate(-getX(), -getY(), 0)
                .scale(getScaleX(), getScaleY(), 0);
    }

    @Override
    public void setScaleX(float x) {
        super.setScaleX(x);
        viewMatrix.identity()
                .rotateZ(getRotationRadians())
                .translate(-getX(), -getY(), 0)
                .scale(getScaleX(), getScaleY(), 0);
    }

    @Override
    public void setScaleY(float y) {
        super.setScaleY(y);
        viewMatrix.identity()
                .rotateZ(getRotationRadians())
                .translate(-getX(), -getY(), 0)
                .scale(getScaleX(), getScaleY(), 0);
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return new Matrix4f(projectionMatrix);
    }

    @Override
    public Matrix4f getViewMatrix() {
        return new Matrix4f(viewMatrix);
    }
}
