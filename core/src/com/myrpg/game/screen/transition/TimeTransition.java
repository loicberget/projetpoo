package com.myrpg.game.screen.transition;

/**
 * TimeTransition class for managing time-based transitions.
 */
public class TimeTransition {

    private float transitionTime; // Total duration of the transition
    private float currentTime;    // Current elapsed time

    private boolean finished = true; // Flag indicating if the transition is finished
    private boolean started = false; // Flag indicating if the transition has started

    /**
     * Checks if the transition has finished.
     *
     * @return true if finished, false otherwise
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Gets the progress of the transition.
     *
     * @return the percentage of completion (0 to 1)
     */
    public float getProgress() {
        if (transitionTime == 0) return 1f; // Avoid division by zero
        return currentTime / transitionTime;
    }

    /**
     * Starts the transition for a given duration.
     *
     * @param time Duration of the transition in seconds
     */
    public void start(float time) {
        this.transitionTime = time;
        this.currentTime = 0;
        this.finished = false;
        this.started = true;
    }

    /**
     * Stops the transition.
     */
    public void stop() {
        this.started = false;
        this.finished = true; // Set finished to true when stopped
    }

    /**
     * Updates the transition's progress.
     *
     * @param deltaTime Time elapsed since last update in seconds
     */
    public void update(float deltaTime) {
        if (!started || finished) return;

        currentTime += deltaTime;
        if (currentTime >= transitionTime) {
            currentTime = transitionTime;
            finished = true;
        }
    }
}
