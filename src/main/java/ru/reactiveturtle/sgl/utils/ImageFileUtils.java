package ru.reactiveturtle.sgl.utils;

import org.lwjgl.opengl.GL11;
import ru.reactiveturtle.sgl.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.*;

public final class ImageFileUtils {
    private ImageFileUtils() {
    }

    public static void makeImage(
            Window window,
            File outputFile,
            ImageFormat imageFormat) {
        int width = window.getWidth();
        int height = window.getHeight();
        int textureId = GL11.glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);

        int framebufferId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferId);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureId, 0);
        if (window.getRenderContext() != null) {
            window.getRenderContext().render();
        }
        float[] pixels = new float[width * height * 4];
        glReadPixels(0, 0, width, height, GL_RGBA, GL_FLOAT, pixels);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        glDeleteFramebuffers(framebufferId);
        glDeleteTextures(textureId);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int widthSize = width * 4;
        for (int i = 0; i < height; i++) {
            int[] intPixels = new int[width];
            for (int j = 0; j < widthSize; j += 4) {
                int index = i * widthSize + j;
                int alpha = Math.round(pixels[index + 3] * 255f);
                int red = Math.round(pixels[index] * 255f);
                int green = Math.round(pixels[index + 1] * 255f);
                int blue = Math.round(pixels[index + 2] * 255f);
                int color = 0;
                color |= (alpha << 24) & 0xFF000000;
                color |= (red << 16) & 0x00FF0000;
                color |= (green << 8) & 0x0000FF00;
                color |= (blue) & 0x000000FF;
                intPixels[j / 4] = color;
            }
            image.setRGB(0, height - i - 1, width, 1, intPixels, 0, width);
        }

        try {
            ImageIO.write(image, map(imageFormat), outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String map(ImageFormat imageFormat) {
        switch (imageFormat) {
            case PNG:
                return "png";
            case JPEG:
                return "jpeg";
            default:
                throw new EnumConstantNotPresentException(ImageFormat.class, "imageFormat");
        }
    }
}
