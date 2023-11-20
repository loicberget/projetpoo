package com.myrpg.game.screen.transition.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.myrpg.game.screen.ScreenType;

/**
 * Implements a rotation transition effect between screens in a LibGDX application.
 */
public class RotationTransitionEffect extends TransitionEffect {

    private final Color color;
    private final ShapeRenderer shapeRenderer;

    /**
     * Constructs a RotationTransitionEffect with a specified duration.
     *
     * @param duration Duration of the rotation effect.
     */
    public RotationTransitionEffect(float duration) {
        super(duration);
        this.color = new Color(0f, 0f, 0f, 1f); // Initialize with black color
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(ScreenType current, ScreenType next) {

    }

    /**
     * Renders the transition effect between the current and next screen.
     *
     * @param current The current screen.
     * @param next    The next screen to transition to.
     */
    @Override
    public void render(Screen current, Screen next) {
        // Render the current screen
        current.render(Gdx.graphics.getDeltaTime());

        // Calculate rotation angle based on alpha value
        float angle = 360 * getAlpha(); // This can be adjusted as needed

        // Set up the rotation matrix
        Matrix4 rotationMatrix = new Matrix4();
        rotationMatrix.setToRotation(0, 0, 1, angle);

        // Enable blending for transparency
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(rotationMatrix);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        // Draw a filled circle (or any other shape as per the requirement)
        shapeRenderer.circle(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Gdx.graphics.getWidth() / 2f);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void render(Screen current, ScreenType next) {

    }


    public void dispose() {
        shapeRenderer.dispose();
    }
}
