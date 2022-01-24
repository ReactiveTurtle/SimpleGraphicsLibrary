package ru.reactiveturtle.sgl;

import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;

public class DisplayMetrics {
    private final GLFWVidMode vidMode;

    public DisplayMetrics() {
        long monitorId = glfwGetPrimaryMonitor();
        vidMode = glfwGetVideoMode(monitorId);
        if (vidMode == null)
            throw new IllegalMonitorStateException("Unable to access display");
    }

    public int getWidth() {
        return vidMode.width();
    }

    public int getHeight() {
        return vidMode.height();
    }
}
