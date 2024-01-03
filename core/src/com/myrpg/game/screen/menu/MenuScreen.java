package com.myrpg.game.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.myrpg.game.audio.AudioObserver;
import com.myrpg.game.manager.ResourceManager;
import com.myrpg.game.rpg_game;
import com.myrpg.game.screen.AbstractScreen;
import com.myrpg.game.screen.ScreenType;

import static com.myrpg.game.audio.AudioObserver.AudioTypeEvent.MENU_THEME;

public class MenuScreen extends AbstractScreen {
    protected Table menuTable;
    protected Stage menuStage = new Stage();
    private Texture backgroundTexture; // New background texture

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


    private void handleNewButton() {
        addButton("New Game", 0, menuTable.getHeight()/10, menuTable);

        Actor newButton = menuTable.getCells().get(0).getActor();
        newButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent even, float x, float y) {
                context.setScreen(ScreenType.MENU_NEW_GAME);
            }
        });

    }

    private void handleLoadButton() {
        addButton("Load Game", 0, menuTable.getHeight()/15, menuTable);
        Actor loadButton = menuTable.getCells().get(1).getActor();
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent even, float x, float y) {
                context.setScreen(ScreenType.MENU_LOAD_GAME);
            }
        });
    }

    private void handleMusicButton() {
        addButton("Music", 0, menuTable.getHeight()/10, menuTable);
        Actor musicButton = menuTable.getCells().get(2).getActor();
        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent even, float x, float y) {
                context.setScreen(ScreenType.MUSIC);
            }
        });
    }

    private void handleExitButton() {
        addButton("Exit", 0, menuTable.getHeight()/9, menuTable);
        Actor exitButton = menuTable.getCells().get(3).getActor();
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent even, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    protected void clearAndRenderBackground(){
        ScreenUtils.clear(0, 0, 0, 1);
        context.getSpriteBatch().begin();
        context.getSpriteBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Draw the background image
        context.getSpriteBatch().end();
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
        context.getSpriteBatch().begin();
        context.getSpriteBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Draw the background image
        context.getSpriteBatch().end();
        menuStage.act(delta);
        menuStage.draw();
    }

    @Override
    public void pause() {    }

    @Override
    public void resume() {    }

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