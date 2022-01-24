package ru.reactiveturtle.app;

import org.joml.Vector2f;
import ru.reactiveturtle.sgl.Application;
import ru.reactiveturtle.sgl.Color;
import ru.reactiveturtle.sgl.Window;
import ru.reactiveturtle.sgl.control.Cursor;
import ru.reactiveturtle.sgl.control.Keyboard;
import ru.reactiveturtle.sgl.shape.Circle;
import ru.reactiveturtle.sgl.shape.Rectangle;
import ru.reactiveturtle.sgl.shape.ShapeRenderContext;
import ru.reactiveturtle.sgl.shape.Triangle;
import ru.reactiveturtle.sgl.utils.ImageFileUtils;
import ru.reactiveturtle.sgl.utils.ImageFormat;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Application application = new Application();
        application.createWindow("Title", 800, 480, new Window.WindowListener() {
            @Override
            public void onInit(Window window) {
                window.show();
                ShapeRenderContext renderContext = new ShapeRenderContext(window);
                renderContext.start();
                window.setRenderContext(renderContext);

                Triangle triangle = new Triangle(
                        new Vector2f(0, 0),
                        new Vector2f(0, 100),
                        new Vector2f(100, 0),
                        1);
                triangle.setFillColor(new Color(0, 0, 255, 255).value());
                triangle.setStrokeColor(new Color(240, 240, 0, 255).value());
                triangle.setX(200);
                renderContext.addShape(triangle);

                Rectangle rectangle = new Rectangle(50, 50, 3);
                rectangle.setFillColor(new Color(254, 0, 0, 255).value());
                rectangle.setStrokeColor(new Color(0, 240, 0, 255).value());
                renderContext.addShape(rectangle);

                Circle circle = new Circle(80, 4);
                circle.setPosition(-100, 100);
                circle.setFillColor(new Color(0, 240, 255, 255).value());
                circle.setStrokeColor(new Color(0, 0, 200, 255).value());
                renderContext.addShape(circle);

                window.getCursor().setListener(new Cursor.Listener() {
                    @Override
                    public void onDown(Vector2f cursorWindowPosition, Cursor.Key key) {
                        System.out.println("CURSOR KEY '" + key + "' DOWN ");

                        triangle.setPoints(
                                new Vector2f(0, 0),
                                new Vector2f(0, 100),
                                renderContext.getCamera().getCursorPosition(window, cursorWindowPosition));
                    }

                    @Override
                    public void onMove(Vector2f cursorPosition, double biasX, double biasY) {
                        System.out.printf(
                                "(X, Y) = {%s, %s}, bias(X, Y) = {%s, %s}%n",
                                cursorPosition.x,
                                cursorPosition.y,
                                biasX,
                                biasY);
                    }

                    @Override
                    public void onUp(Vector2f cursorPosition, Cursor.Key key) {
                        System.out.println("CURSOR KEY '" + key + "' UP");
                    }

                    @Override
                    public void onRepeat(Vector2f cursorPosition, Cursor.Key key) {
                        System.out.println("CURSOR KEY '" + key + "' REPEAT");
                    }
                });

                window.getKeyboard().setListener(new Keyboard.Listener() {
                    @Override
                    public void onDown(Keyboard.Key key) {
                        System.out.println("KEYBOARD KEY '" + key + "' DOWN");
                    }

                    @Override
                    public void onRepeat(Keyboard.Key key) {
                        System.out.println("KEYBOARD KEY '" + key + "' REPEAT");
                    }

                    @Override
                    public void onUp(Keyboard.Key key) {
                        System.out.println("KEYBOARD KEY '" + key + "' UP");
                    }
                });
            }

            int i = 0;

            @Override
            public void onFrameRenderEnd(Window window) {
                if (i == 0) {
                    ImageFileUtils.makeImage(window, new File("screenshot.jpg"), ImageFormat.PNG);
                    i++;
                }
            }
        });

        application.createWindow("Title", 800, 480, new Window.WindowListener() {
            @Override
            public void onInit(Window window) {
                window.show();
            }

            @Override
            public void onFrameRenderEnd(Window window) {

            }
        });
    }
}
