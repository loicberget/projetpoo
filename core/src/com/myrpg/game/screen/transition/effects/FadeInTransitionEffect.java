package com.myrpg.game.screen.transition.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.myrpg.game.screen.transition.ImmediateModeRendererUtils;
import com.myrpg.game.screen.ScreenType;

/**
 * Implements a fade-in transition effect between screens in a LibGDX application.
 */
public class FadeInTransitionEffect extends TransitionEffect {

    private final Color color;

    /**
     * Constructs a FadeInTransitionEffect with a specified duration.
     *
     * @param duration Duration of the fade-in effect.
     */
    public FadeInTransitionEffect(float duration) {
        super(duration);
        this.color = new Color(0f, 0f, 0f, 1f); // Initialize with black color and full opacity
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
        // Render the next screen
        next.render(Gdx.graphics.getDeltaTime());

        // Set the alpha for the fade effect
        color.a = 1f - getAlpha();

        // Enable blending for transparency
        Gdx.gl.glEnable(GL20.GL_BLEND);
        // Set the projection matrix for rendering
        ImmediateModeRendererUtils.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Render a rectangle over the screen with the fade color
        ImmediateModeRendererUtils.fillRectangle(0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), color);
        // Disable blending
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void render(Screen current, ScreenType next) {

    }
}
