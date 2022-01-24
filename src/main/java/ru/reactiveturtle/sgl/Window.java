package ru.reactiveturtle.sgl;

import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.opengl.GL;
import ru.reactiveturtle.sgl.control.Cursor;
import ru.reactiveturtle.sgl.control.Keyboard;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements WindowImpl {
    private Application application;
    private int width;
    private int height;

    private long windowId;
    private boolean isWindowFocused;

    private Cursor cursor;
    private Keyboard keyboard;
    private RenderContext renderContext;
    public Window(Application application,
                  String title,
                  int width,
                  int height,
                  WindowListener windowListener) {
        this.application = Objects.requireNonNull(application);
        this.width = width;
        this.height = height;

        Thread renderThread = new Thread(() -> {
            init(title, width, height);
            if (windowListener != null) {
                windowListener.onInit(Window.this);
            }
            startRender(windowListener);
            startDispose();
        });
        renderThread.start();
    }

    @Override
    public void show() {
        glfwShowWindow(windowId);
        glfwFocusWindow(windowId);
        isWindowFocused = true;
    }

    @Override
    public void fullscreen() {

    }

    @Override
    public void setRenderContext(RenderContext renderContext) {
        this.renderContext = renderContext;
    }

    @Override
    public RenderContext getRenderContext() {
        return renderContext;
    }

    private void init(String title, int width, int height) {
        windowId = glfwCreateWindow(width, height, title, 0, NULL);
        if (windowId == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwSetWindowFocusCallback(windowId, new GLFWWindowFocusCallback() {
            @Override
            public void invoke(long window, boolean focused) {
                isWindowFocused = focused;
            }
        });

        glfwMakeContextCurrent(windowId);

        cursor = new Cursor(this);
        keyboard = new Keyboard(this);
        if (renderContext != null) {
            renderContext.start();
        }

        GL.createCapabilities();
        glfwSetWindowSizeCallback(windowId, (windowId, width1, height1) -> {
            Window.this.width = width1;
            Window.this.height = height1;
            if (renderContext != null) {
                glViewport(0, 0, width1, height1);
                renderContext.onChangeWindowSize(width1, height1);
            }
        });

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glfwSwapInterval(1);
    }

    private double deltaTime;
    private double lastTime;

    private void startRender(WindowListener windowListener) {
        lastTime = glfwGetTime();
        while (!glfwWindowShouldClose(windowId)) {
            if (isWindowFocused) {
                glfwPollEvents();
                deltaTime = glfwGetTime() - lastTime;
                lastTime = glfwGetTime();
                if (renderContext != null) {
                    renderContext.render();
                }
                glfwSwapBuffers(windowId);
                if (windowListener != null) {
                    windowListener.onFrameRenderEnd(this);
                }
            } else {
                lastTime = glfwGetTime();
                glfwWaitEvents();
            }
        }
    }

    private void startDispose() {
        glfwFreeCallbacks(windowId);
        glfwDestroyWindow(windowId);

        if (renderContext != null) {
            renderContext.dispose();
        }
    }

    public long getWindowId() {
        return windowId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public interface WindowListener {
        void onInit(Window window);

        void onFrameRenderEnd(Window window);
    }
}
