package com.myrpg.game.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.myrpg.game.audio.AudioObserver;
import com.myrpg.game.manager.ResourceManager;
import com.myrpg.game.rpg_game;
import com.myrpg.game.screen.AbstractScreen;
import com.myrpg.game.screen.ScreenType;

import java.util.ArrayList;

import static com.myrpg.game.audio.AudioObserver.AudioTypeEvent.MENU_THEME;

public class MenuScreen extends AbstractScreen {
    private Table menuTable;
    protected Stage menuStage = new Stage();
    private Texture backgroundTexture; // New background texture
    private final ScreenType currentScreen = getScreenClass();
    private float stateTime;

    public MenuScreen(final rpg_game context, ResourceManager resourceManager) {
        super(context, resourceManager);
        super.musicTheme = MENU_THEME;


        menuTable = createTable();
        backgroundTexture = new Texture("background/natureBackground_frames_sheet.png");
        handleNewButton();
        handleLoadButton();
        handleMusicButton();
        handleExitButton();
    }

    @Override
    protected Table getScreenUI(Skin skin) {
        return null;
    }


    private void handleExitButton() {
        createButton("Exit", 0, menuTable.getHeight()/9, menuTable);
        Actor exitButton = menuTable.getCells().get(3).getActor();
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent even, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    private void handleMusicButton() {
        createButton("Music", 0, menuTable.getHeight()/10, menuTable);
        Actor musicButton = menuTable.getCells().get(2).getActor();
        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent even, float x, float y) {
                setScreenWithTransition(getScreenClass(), ScreenType.MUSIC, new ArrayList<>());
            }
        });
    }

    private void handleNewButton() {
        createButton("New Game", 0, menuTable.getHeight()/10, menuTable);

        Actor newButton = menuTable.getCells().get(0).getActor();
        newButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent even, float x, float y) {
                // menuTable.remove(); // TODO: Does not work
                setScreenWithTransition(getScreenClass(), ScreenType.MENU_NEW_GAME, new ArrayList<>());
            }
        });
    }

    private void handleLoadButton() {
        createButton("Load Game", 0, menuTable.getHeight()/15, menuTable);
        Actor loadButton = menuTable.getCells().get(1).getActor();
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent even, float x, float y) {
                setScreenWithTransition(getScreenClass(), ScreenType.MENU_LOAD_GAME, new ArrayList<>());
            }
        });
    }

    @Override
    public void show() {
        menuStage.addActor(menuTable);
        Gdx.input.setInputProcessor(menuStage);
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, MENU_THEME);
        notify(AudioObserver.AudioCommand.MUSIC_PLAY_LOOP, MENU_THEME);
    }

    @Override
    public void render(float delta) {
        stateTime += Gdx.graphics.getDeltaTime();
        context.getSpriteBatch().begin();
        context.getSpriteBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Draw the background image
        context.getSpriteBatch().end();
        //if (!resourceManager.isMusicScreen() && !resourceManager.isMenuNewGameScreen() && !resourceManager.isMenuLoadGameScreen()) {
            menuStage.act(delta);
            menuStage.draw();
        //} else {
            //menuStage.clear();
        //}
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        super.dispose();
        menuTable.remove();
        backgroundTexture.dispose(); // Dispose of the background texture
    }

    @Override
    public void resize(int width, int height) {    }

    @Override
    public void hide() {    }
}
