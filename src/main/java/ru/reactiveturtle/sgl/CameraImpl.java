package ru.reactiveturtle.sgl;

import org.joml.Matrix4f;

public interface CameraImpl {
    Matrix4f getProjectionMatrix();

    Matrix4f getViewMatrix();
}
