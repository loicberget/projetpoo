package com.myrpg.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;
import com.myrpg.game.rpg_game;

public class LoadingScreen extends AbstractScreen {
    // private final rpg_game context;

    public LoadingScreen(final rpg_game context) {
        super(context);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // color of the loading screen
        ScreenUtils.clear(0, 1, 0, 1);

        // if any key is pressed -> change the screen
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            context.setScreen(ScreenType.GAME);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
