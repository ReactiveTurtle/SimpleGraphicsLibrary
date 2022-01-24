package ru.reactiveturtle.sgl;

import org.lwjgl.opengl.GL11;

public abstract class RenderContext {
    private final Window window;
    private CameraImpl camera;

    public RenderContext(Window window){
        this.window = window;
    }

    public void clearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
    }

    public void clearColor(Color color) {
        clearColor(color.redFloat(), color.greenFloat(), color.blueFloat(), color.alphaFloat());
    }

    public void clearColorBufferBit() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public abstract void start();

    public abstract void onChangeWindowSize(int width, int height);

    public abstract void render();

    public abstract void dispose();

    public Window getWindow() {
        return window;
    }

    public void setCamera(CameraImpl camera) {
        this.camera = camera;
    }

    public CameraImpl getCamera() {
        return camera;
    }
}
