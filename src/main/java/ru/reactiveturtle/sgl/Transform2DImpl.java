package ru.reactiveturtle.sgl;

import org.joml.Vector2f;

public interface Transform2DImpl {
    void setOriginX(float x);

    void setOriginY(float y);

    void setOrigin(float x, float y);

    void setOrigin(Vector2f origin);

    void setX(float x);

    void setY(float y);

    void setPosition(float x, float y);

    float getX();

    float getY();

    Vector2f getPosition();

    void setPosition(Vector2f position);

    void setRotationAngle(float angle);

    void setRotationRadians(float rotationRadians);

    float getRotationAngle();

    float getRotationRadians();

    void setScale(float scale);

    void setScale(float scaleX, float scaleY);

    void setScale(Vector2f scale);

    void setScaleX(float x);

    void setScaleY(float y);

    Vector2f getScale();

    float getScaleX();

    float getScaleY();
}
