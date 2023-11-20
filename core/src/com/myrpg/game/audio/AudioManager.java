package com.myrpg.game.audio;

import com.badlogic.gdx.audio.Music;
import com.myrpg.game.manager.PreferenceManager;
import com.myrpg.game.manager.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;

/**
 * Singleton class that manages audio playback.
 */
public class AudioManager implements AudioObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioManager.class);
    private static AudioManager instance = null;

    private Music currentMusic;
    private Hashtable<String, Music> queuedMusic;

    private AudioManager() {
        queuedMusic = new Hashtable<>();
    }

    /**
     * Gets the singleton instance of AudioManager.
     *
     * @return The singleton instance.
     */
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    /**
     * Gets the currently playing music.
     *
     * @return The current music.
     */
    public Music getCurrentMusic() {
        return currentMusic;
    }

    /**
     * Sets the current music.
     *
     * @param currentMusic The music to be set as current.
     */
    public void setCurrentMusic(Music currentMusic) {
        this.currentMusic = currentMusic;
    }

    /**
     * Checks if music is enabled in preferences and plays/stops accordingly.
     *
     * @param music The music to be checked and played.
     */
    private void checkMusicEnabled(Music music) {
        if (!PreferenceManager.getInstance().isMusicEnabled()) {
            music.stop();
        } else {
            music.play();
        }
    }

    /**
     * Handles audio commands and events.
     *
     * @param command The audio command.
     * @param event The type of audio event.
     */
    @Override
    public void onNotify(AudioCommand command, AudioTypeEvent event) {
        switch (command) {
            case MUSIC_LOAD:
                ResourceManager.loadMusicAsset(event.getValue());
                break;
            case MUSIC_PLAY_ONCE:
                playMusic(false, event.getValue());
                break;
            case MUSIC_PLAY_LOOP:
                playMusic(true, event.getValue());
                break;
            case MUSIC_STOP:
                stopMusic(event.getValue());
                break;
            case MUSIC_STOP_ALL:
                stopAllMusic();
                break;
            default:
                // No action for other commands
                break;
        }
    }

    /**
     * Plays the specified music file.
     *
     * @param isLooping Whether the music should loop.
     * @param fullFilePath The file path of the music.
     */
    private void playMusic(boolean isLooping, String fullFilePath) {
        Music music = queuedMusic.get(fullFilePath);
        if (music == null && ResourceManager.isAssetLoaded(fullFilePath)) {
            music = ResourceManager.getMusicAsset(fullFilePath);
            queuedMusic.put(fullFilePath, music);
        }
        if (music != null) {
            configureAndPlayMusic(music, isLooping);
        } else {
            LOGGER.debug("Music not loaded");
        }
    }

    /**
     * Configures and plays the given music.
     *
     * @param music The music to play.
     * @param isLooping Whether the music should loop.
     */
    private void configureAndPlayMusic(Music music, boolean isLooping) {
        music.setLooping(isLooping);
        music.setVolume(PreferenceManager.getMusicVolume());
        checkMusicEnabled(music);
        setCurrentMusic(music);
    }

    /**
     * Stops the specified music.
     *
     * @param musicKey The key of the music to stop.
     */
    private void stopMusic(String musicKey) {
        Music music = queuedMusic.get(musicKey);
        if (music != null) {
            music.stop();
        }
    }

    /**
     * Stops all queued music.
     */
    private void stopAllMusic() {
        for (Music music : queuedMusic.values()) {
            music.stop();
        }
    }

    /**
     * Disposes of all music resources.
     */
    public void dispose() {
        for (Music music : queuedMusic.values()) {
            music.dispose();
        }
        queuedMusic.clear();
    }
}
