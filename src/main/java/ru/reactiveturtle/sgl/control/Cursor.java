package ru.reactiveturtle.sgl.control;

import org.joml.Vector2f;
import ru.reactiveturtle.sgl.Window;

import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;

public class Cursor {
    private final Window window;

    public Cursor(Window window) {
        Objects.requireNonNull(window);
        this.window = window;
    }

    public void setListener(Listener listener) {
        if (listener == null) {
            glfwSetCursorPosCallback(window.getWindowId(), null);
            glfwSetMouseButtonCallback(window.getWindowId(), null);
            return;
        }
        Vector2f lastCursorPosition = getPosition();
        final double[] lastX = {lastCursorPosition.x};
        final double[] lastY = {lastCursorPosition.y};
        glfwSetCursorPosCallback(window.getWindowId(), (windowId, xpos, ypos) -> {
            listener.onMove(new Vector2f((float) xpos, (float) ypos), xpos - lastX[0], ypos - lastY[0]);
            lastX[0] = xpos;
            lastY[0] = ypos;
        });
        glfwSetMouseButtonCallback(window.getWindowId(), (windowId, button, action, mods) -> {
            Vector2f cursorPosition = getPosition();
            switch (action) {
                case GLFW_RELEASE:
                    listener.onUp(cursorPosition, map(button));
                    break;
                case GLFW_PRESS:
                    listener.onDown(cursorPosition, map(button));
                    break;
                case GLFW_REPEAT:
                    listener.onRepeat(cursorPosition, map(button));
                    break;
            }
        });
    }

    private static Key map(int glfwKey) {
        switch (glfwKey) {
            case GLFW_MOUSE_BUTTON_LEFT:
                return Key.LEFT;
            case GLFW_MOUSE_BUTTON_MIDDLE:
                return Key.MIDDLE;
            case GLFW_MOUSE_BUTTON_RIGHT:
                return Key.RIGHT;
            default:
                throw new RuntimeException("Key " + glfwKey + " not present.");
        }
    }

    public Vector2f getPosition() {
        double[] xPos = new double[1];
        double[] yPos = new double[1];
        glfwGetCursorPos(window.getWindowId(), xPos, yPos);
        return new Vector2f((float) xPos[0], (float) yPos[0]);
    }

    public static class Listener {
        public void onDown(Vector2f cursorPosition, Key key) {
        }

        public void onMove(Vector2f cursorPosition, double biasX, double biasY) {
        }

        public void onUp(Vector2f cursorPosition, Key key) {
        }

        public void onRepeat(Vector2f cursorPosition, Key key) {
        }
    }

    public enum Key {
        LEFT,
        MIDDLE,
        RIGHT
    }
}
