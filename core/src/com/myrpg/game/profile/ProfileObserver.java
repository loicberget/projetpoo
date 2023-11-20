package com.myrpg.game.profile;

/**
 * Represents an observer in the observer pattern, specifically for profile events.
 */
public interface ProfileObserver {

    /**
     * Enumerates the types of profile-related events that can be observed.
     */
    enum ProfileEvent {
        PROFILE_LOADED,         // Triggered when a profile is successfully loaded
        SAVING_PROFILE,         // Triggered when a profile is about to be saved
        CLEAR_CURRENT_PROFILE   // Triggered when the current profile needs to be cleared
    }

    /**
     * Called when a profile-related event occurs.
     *
     * @param profileManager The instance of ProfileManager that triggered the event.
     * @param event The type of profile event that occurred.
     */
    void onNotify(final ProfileManager profileManager, ProfileEvent event);
}
