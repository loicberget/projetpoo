package com.myrpg.game.audio;

/**
 * An interface for observing audio-related events and commands.
 */
public interface AudioObserver {

    /**
     * Enum representing different types of audio events, each associated with a specific audio file.
     */
    enum AudioTypeEvent {
        //MENU_THEME("music/Rising_Sun.mp3"),
        //MENU_THEME("music/Village.mp3"),
        // MENU_THEME("music/Dwarves'_Theme.mp3"),
        MENU_THEME("music/Challenge.mp3"),
       // MENU_THEME("music/Requiem.mp3"),

        GAME_THEME("music/Village.mp3"),
        TEST_THEME("music/Dwarves'_Theme.mp3"),
        BATTLE_THEME("music/Challenge.mp3"),
        GAME_OVER_THEME("music/Requiem.mp3"),
        NONE(""); // Represents no specific audio event

        private String audioFullFilePath;

        AudioTypeEvent(String audioFullFilePath) {
            this.audioFullFilePath = audioFullFilePath;
        }

        /**
         * Gets the file path of the audio file associated with this event.
         *
         * @return the full file path of the audio file
         */
        public String getValue() {
            return audioFullFilePath;
        }
    }

    /**
     * Enum representing different audio commands that can be issued.
     */
    enum AudioCommand {
        MUSIC_LOAD,         // Command to load music
        MUSIC_PLAY_ONCE,    // Command to play music once
        MUSIC_PLAY_LOOP,    // Command to loop music playback
        MUSIC_STOP,         // Command to stop music
        MUSIC_STOP_ALL      // Command to stop all music
    }

    /**
     * Method to be called when an audio event occurs.
     *
     * @param command the command related to the audio event
     * @param event the type of audio event
     */
    void onNotify(AudioCommand command, AudioTypeEvent event);
}
