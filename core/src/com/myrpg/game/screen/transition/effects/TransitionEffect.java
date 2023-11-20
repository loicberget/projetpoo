package com.myrpg.game.screen.transition.effects;

import com.badlogic.gdx.Screen;
import com.myrpg.game.screen.ScreenType;
import com.myrpg.game.screen.transition.TimeTransition;

/**
 * Base class for transition effects between screens.
 */
public abstract class TransitionEffect {

    private final TimeTransition timeTransition;

    /**
     * Constructs a TransitionEffect with a specified duration.
     *
     * @param duration The duration of the transition effect.
     */
    public TransitionEffect(float duration) {
        this.timeTransition = new TimeTransition();
        this.timeTransition.start(duration);
    }

    /**
     * Returns the alpha value representing the progress of the transition.
     *
     * @return A float value between 0 and 1 indicating transition progress.
     */
    protected float getAlpha() {
        return timeTransition.getProgress(); // Updated method name
    }

    /**
     * Updates the transition effect over time.
     *
     * @param delta The time in seconds since the last frame.
     */
    public void update(float delta) {
        timeTransition.update(delta);
    }

    /**
     * Renders the transition effect. Subclasses should override this method to provide specific rendering logic.
     *
     * @param current The current screen.
     * @param next The next screen to transition to.
     */
    public abstract void render(ScreenType current, ScreenType next);

    /**
     * Checks if the transition effect is completed.
     *
     * @return True if the transition is finished, otherwise false.
     */
    public boolean isFinished() {
        return timeTransition.isFinished();
    }

    public abstract void render(Screen current, Screen next);

    public abstract void render(Screen current, ScreenType next);
}
