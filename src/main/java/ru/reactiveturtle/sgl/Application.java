package ru.reactiveturtle.sgl;

import org.lwjgl.glfw.GLFWErrorCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;

public class Application implements ApplicationImpl, Disposable {
    private DisplayMetrics displayMetrics;
    private List<Window> windowList = new ArrayList<>();

    public Application() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        displayMetrics = new DisplayMetrics();
    }

    @Override
    public void createWindow(String title, int width, int height, Window.WindowListener windowListener) {
        Window window = new Window(this, title, width, height, windowListener);
        windowList.add(window);
    }

    @Override
    public DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }

    @Override
    public void dispose() {
        windowList.clear();
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
}
