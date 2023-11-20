package com.myrpg.game.screen.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

/**
 * Utility class for immediate mode rendering in LibGDX.
 */
public class ImmediateModeRendererUtils {
    private static final ImmediateModeRenderer renderer = new ImmediateModeRenderer20(true, true, 0);
    private static final Matrix4 projectionMatrix = new Matrix4();

    private ImmediateModeRendererUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets the projection matrix, initializing it if necessary.
     *
     * @return The projection matrix.
     */
    public static Matrix4 getProjectionMatrix() {
        projectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return projectionMatrix;
    }

    /**
     * Draws a filled arc circle at a specified position.
     *
     * @param x      X-coordinate of the circle center.
     * @param y      Y-coordinate of the circle center.
     * @param radius Radius of the circle.
     * @param color  Color of the circle.
     */
    public static void drawFillArcCircle(float x, float y, float radius, Color color) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.arc(x, y, radius, 0, 360, 24);
        shapeRenderer.end();
        shapeRenderer.dispose();
    }

    /**
     * Fills a rectangle with the specified color.
     *
     * @param x0    X-coordinate of the bottom left corner.
     * @param y0    Y-coordinate of the bottom left corner.
     * @param x1    X-coordinate of the top right corner.
     * @param y1    Y-coordinate of the top right corner.
     * @param color Color of the rectangle.
     */
    public static void fillRectangle(float x0, float y0, float x1, float y1, Color color) {
        renderer.begin(getProjectionMatrix(), GL20.GL_TRIANGLES);

        // Define vertices for two triangles forming the rectangle
        renderer.color(color.r, color.g, color.b, color.a);
        renderer.vertex(x0, y0, 0);
        renderer.vertex(x0, y1, 0);
        renderer.vertex(x1, y1, 0);

        renderer.color(color.r, color.g, color.b, color.a);
        renderer.vertex(x1, y1, 0);
        renderer.vertex(x1, y0, 0);
        renderer.vertex(x0, y0, 0);

        renderer.end();
    }
}
