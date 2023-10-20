package com.myrpg.game.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.myrpg.game.rpg_game;

public class LoadingScreen extends AbstractScreen {
    private final AssetManager assetManager;

    public LoadingScreen(final rpg_game context) {
        super(context);

        this.assetManager = context.getAssetManager();
        assetManager.load("map/map.tmx", TiledMap.class);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // color of the loading screen
        ScreenUtils.clear(0, 1, 0, 1);


        // once the asset manager is done loading -> change the screen
        if(assetManager.update())
            context.setScreen(ScreenType.GAME);
        /*
        // if any key is pressed -> change the screen
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            context.setScreen(ScreenType.GAME);
        }*/
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