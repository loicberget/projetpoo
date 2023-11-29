package com.myrpg.game.screen;

import com.badlogic.gdx.Screen;
import com.myrpg.game.audio.AudioObserver;
import com.myrpg.game.screen.menu.MenuLoadGameScreen;
import com.myrpg.game.screen.menu.MenuNewGameScreen;
import com.myrpg.game.screen.menu.MenuScreen;
import com.myrpg.game.screen.menu.MusicScreen;

public enum ScreenType {
    GAME(GameScreen.class),
    LOADING(LoadingScreen.class),
    MENU(MenuScreen.class),
    MENU_LOAD_GAME(MenuLoadGameScreen.class),
    MENU_NEW_GAME(MenuNewGameScreen.class),
    CHARACTER_SELECTION(CharacterSelectionScreen.class),
    MUSIC(MusicScreen.class),
    ;

    private final Class<? extends AbstractScreen> screenClass;

    // constructor
    ScreenType(Class<? extends AbstractScreen> screenClass) {
        this.screenClass = screenClass;
    }

    public Class<? extends Screen> getScreenClass() {
        return screenClass;
    }

    public static ScreenType getScreenTypeByClass(Class<? extends Screen> screenClass) {
        ScreenType result = null;
        for (ScreenType screenType : ScreenType.values()) {
            if (screenType.getScreenClass().equals(screenClass)) {
                result = screenType;
            }
        }
        return result;
    }

    public AudioObserver.AudioTypeEvent getMusicTheme() { // New method
        switch (this) {
            case GAME:
                return AudioObserver.AudioTypeEvent.GAME_THEME;
            case MENU:
                return AudioObserver.AudioTypeEvent.MENU_THEME;

            default:
                return null;
        }
    }
}
