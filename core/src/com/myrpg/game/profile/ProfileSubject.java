package com.myrpg.game.profile;

import com.badlogic.gdx.utils.Array;

/**
 * Serves as the subject in an observer pattern for profile management.
 * It notifies observers about profile-related events.
 */
public class ProfileSubject {

    private Array<ProfileObserver> observers;

    public ProfileSubject() {
        observers = new Array<>();
    }

    /**
     * Adds an observer to the list.
     *
     * @param profileObserver The observer to be added.
     */
    public void addObserver(ProfileObserver profileObserver) {
        if (!observers.contains(profileObserver, true)) {
            observers.add(profileObserver);
        }
    }

    /**
     * Removes an observer from the list.
     *
     * @param profileObserver The observer to be removed.
     */
    public void removeObserver(ProfileObserver profileObserver) {
        observers.removeValue(profileObserver, true);
    }

    /**
     * Removes all observers from the list.
     */
    public void removeAllObservers() {
        observers.clear();
    }

    /**
     * Notifies all observers about a specific profile event.
     *
     * @param profileManager The profile manager instance triggering the event.
     * @param event          The profile event that occurred.
     */
    protected void notify(final ProfileManager profileManager, ProfileObserver.ProfileEvent event) {
        for (ProfileObserver observer : observers) {
            observer.onNotify(profileManager, event);
        }
    }
}
