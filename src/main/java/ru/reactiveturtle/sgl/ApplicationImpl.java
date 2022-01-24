package ru.reactiveturtle.sgl;

public interface ApplicationImpl {
    void createWindow(String title, int width, int height, Window.WindowListener windowListener);

    DisplayMetrics getDisplayMetrics();
}
