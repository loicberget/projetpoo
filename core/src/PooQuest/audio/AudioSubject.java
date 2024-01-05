package PooQuest.audio;

/**
 * An interface for subjects in the observer pattern, specific to audio events.
 */
public interface AudioSubject {

    /**
     * Adds an observer to the subject.
     *
     * @param observer The observer to be added.
     */

    void addObserver(AudioObserver observer);


    /**
     * Removes an observer from the subject.
     *
     * @param audioObserver The observer to be removed.
     */
    void removeObserver(AudioObserver audioObserver);

    /**
     * Removes all observers from the subject.
     */
    void removeAllObservers();

    /**
     * Notifies all observers of an audio event.
     *
     * @param command The audio command associated with the event.
     * @param event The type of audio event.
     */
    void notify(final AudioObserver.AudioCommand command, AudioObserver.AudioTypeEvent event);
}
