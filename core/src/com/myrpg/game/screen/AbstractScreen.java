package com.myrpg.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.myrpg.game.rpg_game;

public abstract class AbstractScreen implements Screen {
    protected final rpg_game context;
    protected final FitViewport viewport;
    protected final World world;
    protected final Box2DDebugRenderer box2DDebugRenderer;
    protected final Stage stage;
    protected final Table screenUI;
    public AbstractScreen(final rpg_game context) {
        this.context = context;
        viewport = context.getScreenViewport();
        this.world = context.getWorld();
        this.box2DDebugRenderer = context.getBox2DDebugRenderer();

        stage = context.getStage();
        screenUI = getScreenUI(context.getSkin());
    }

    protected abstract Table getScreenUI(final Skin skin);

    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        stage.addActor(screenUI);
    }

    @Override
    public void hide() {
        stage.getRoot().removeActor(screenUI);
    }
    @Override
    public void dispose() {
        stage.getRoot().removeActor(screenUI);
    }
}
