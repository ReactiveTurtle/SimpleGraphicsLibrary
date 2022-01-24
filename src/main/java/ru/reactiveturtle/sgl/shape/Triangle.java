package ru.reactiveturtle.sgl.shape;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import ru.reactiveturtle.sgl.Color;

import java.util.Objects;

public class Triangle extends Shape {
    private Vector2f firstPoint;
    private Vector2f secondPoint;
    private Vector2f thirdPoint;

    public Triangle(Vector2f firstPoint,
                    Vector2f secondPoint,
                    Vector2f thirdPoint,
                    float strokeWidth) {
        super(
                getFillVertices(firstPoint, secondPoint, thirdPoint),
                getFillIndices(),
                getStrokeVertices(firstPoint, secondPoint, thirdPoint, strokeWidth),
                getStrokeIndices());
        this.firstPoint = new Vector2f(firstPoint);
        this.secondPoint = new Vector2f(secondPoint);
        this.thirdPoint = new Vector2f(thirdPoint);
        setStrokeWidth(strokeWidth);
    }

    public void setPoints(
            Vector2f firstPoint,
            Vector2f secondPoint,
            Vector2f thirdPoint) {
        this.firstPoint = new Vector2f(firstPoint);
        this.secondPoint = new Vector2f(secondPoint);
        this.thirdPoint = new Vector2f(thirdPoint);
        super.setFillVertices(
                getFillVertices(firstPoint, secondPoint, thirdPoint),
                getFillIndices());
        super.setStrokeVertices(
                getStrokeVertices(firstPoint, secondPoint, thirdPoint, getStrokeWidth()),
                getStrokeIndices());
    }

    @Override
    public void setStrokeWidth(float strokeWidth) {
        super.setStrokeWidth(strokeWidth);
        super.setStrokeVertices(
                getStrokeVertices(firstPoint, secondPoint, thirdPoint, strokeWidth),
                getStrokeIndices());
    }

    @Override
    public void draw(ShapeShader shapeShader) {
        GL30.glBindVertexArray(getFillVertexArrayId());
        GL20.glEnableVertexAttribArray(0);
        shapeShader.loadColor(new Color(getFillColor()).getComponentsAsVector());
        GL20.glDrawElements(GL11.GL_TRIANGLES, getFillVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(getFillVertexArrayId());

        GL30.glBindVertexArray(getStrokeVertexArrayId());
        GL20.glEnableVertexAttribArray(0);
        shapeShader.loadColor(new Color(getStrokeColor()).getComponentsAsVector());
        GL20.glDrawElements(GL11.GL_TRIANGLES, getStrokeVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(getStrokeVertexArrayId());
        GL30.glBindVertexArray(0);
    }

    private static float[] getFillVertices(Vector2f firstPoint,
                                           Vector2f secondPoint,
                                           Vector2f thirdPoint) {
        return new float[]{
                firstPoint.x, firstPoint.y,
                secondPoint.x, secondPoint.y,
                thirdPoint.x, thirdPoint.y
        };
    }

    private static int[] getFillIndices() {
        return new int[]{0, 1, 2};
    }

    private static float[] getStrokeVertices(Vector2f firstPoint,
                                             Vector2f secondPoint,
                                             Vector2f thirdPoint,
                                             float strokeWidth) {
        Vector2f firstStrokePoint = strokeLine(secondPoint, firstPoint, strokeWidth)
                .intersect(strokeLine(firstPoint, thirdPoint, strokeWidth));
        Vector2f secondStrokePoint = strokeLine(thirdPoint, secondPoint, strokeWidth)
                .intersect(strokeLine(secondPoint, firstPoint, strokeWidth));
        Vector2f thirdStrokePoint = strokeLine(firstPoint, thirdPoint, strokeWidth)
                .intersect(strokeLine(thirdPoint, secondPoint, strokeWidth));
        return new float[]{
                firstPoint.x, firstPoint.y,
                secondPoint.x, secondPoint.y,
                thirdPoint.x, thirdPoint.y,

                firstStrokePoint.x, firstStrokePoint.y,
                secondStrokePoint.x, secondStrokePoint.y,
                thirdStrokePoint.x, thirdStrokePoint.y
        };
    }

    private static int[] getStrokeIndices() {
        return new int[]{
                0, 3, 4,
                4, 0, 1,

                1, 4, 5,
                5, 1, 2,

                2, 5, 3,
                3, 2, 0
        };
    }

    private static Line strokeLine(Vector2f firstPoint,
                                   Vector2f secondPoint,
                                   float strokeWidth) {
        Vector2f firstSecondLine = new Vector2f(secondPoint).sub(firstPoint);
        Vector2f firstSecondLinePerpendicularBias = firstSecondLine.perpendicular().normalize(strokeWidth);
        Vector2f strokeFirstPoint = new Vector2f(firstPoint)
                .add(firstSecondLinePerpendicularBias);
        Vector2f strokeSecondPoint = new Vector2f(secondPoint)
                .add(firstSecondLinePerpendicularBias);
        return new Line(strokeFirstPoint, strokeSecondPoint);
    }

    private static class Line {
        private final float aComponent, bComponent, cComponent;

        public Line(Vector2f firstPoint, Vector2f secondPoint) {
            if (firstPoint.equals(secondPoint)) {
                throw new IllegalArgumentException("Impossible to build line");
            }
            Vector2f deltaSecondFirst = new Vector2f(secondPoint).sub(firstPoint);
            if (deltaSecondFirst.x != 0) {
                aComponent = deltaSecondFirst.y / deltaSecondFirst.x;
                bComponent = -1;
                cComponent = aComponent * -firstPoint.x + firstPoint.y;
            } else /* if (deltaSecondFirst.y != 0) */ {
                aComponent = 1;
                bComponent = -(deltaSecondFirst.x / deltaSecondFirst.y);
                cComponent = bComponent * -firstPoint.y - firstPoint.x;
            }
        }

        public Vector2f intersect(Line line) {
            if (this.equals(line)) {
                throw new IllegalArgumentException("This line is equals to argument line");
            }
            if (this.aComponent != 0) {
                float y = (line.aComponent * this.cComponent - this.aComponent * line.cComponent) /
                        (this.aComponent * line.bComponent - line.aComponent * this.bComponent);
                float x = -(this.bComponent * y + this.cComponent) / this.aComponent;
                return new Vector2f(x, y);
            } else if (this.bComponent != 0) {
                float x = (line.bComponent * this.cComponent - this.bComponent * line.cComponent) /
                        (line.aComponent * this.bComponent - this.aComponent * line.bComponent);
                float y = -(this.aComponent * x + this.cComponent) / this.bComponent;
                return new Vector2f(x, y);
            }
            throw new IllegalStateException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Line line = (Line) o;
            return Float.compare(line.aComponent, aComponent) == 0 &&
                    Float.compare(line.bComponent, bComponent) == 0 &&
                    Float.compare(line.cComponent, cComponent) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(aComponent, bComponent, cComponent);
        }
    }
}
