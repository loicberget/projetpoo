package com.myrpg.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.myrpg.game.audio.AudioManager;
import com.myrpg.game.audio.AudioObserver;
import com.myrpg.game.audio.AudioSubject;
import com.myrpg.game.manager.ResourceManager;
import com.myrpg.game.rpg_game;

public abstract class AbstractScreen implements Screen, AudioSubject {
    protected final rpg_game context;
    protected ResourceManager resourceManager;
    private Array<AudioObserver> observers;
    protected FitViewport viewport;
    protected World world;
    protected Box2DDebugRenderer box2DDebugRenderer;
    protected Stage stage;
    protected Table screenUI;
    protected  AudioObserver.AudioTypeEvent musicTheme;

    public AbstractScreen(final rpg_game context, ResourceManager resourceManager) {
        this.context = context;
        this.resourceManager = resourceManager;
        observers = new Array<>();
        addObserver(AudioManager.getInstance());

        viewport = context.getScreenViewport();
        this.world = context.getWorld();
        this.box2DDebugRenderer = context.getBox2DDebugRenderer();

        stage = context.getStage();
        screenUI = getScreenUI(context.getSkin());
    }
    public AudioObserver.AudioTypeEvent getMusicTheme() {
        return musicTheme;
    }

    public Table createTable() {
        Table table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return table;
    }

    private TextureRegionDrawable createDrawableWithColor(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    public void addButton(String buttonName, float posX, float posY, Table table) {
        BitmapFont font = resourceManager.pixel10Bold; // Ensure this font is correctly initialized in ResourceManager
        TextureRegionDrawable imageUp = createDrawableWithColor(70, 30, new Color(1, 0.65f, 0, 1));
        TextureRegionDrawable imageDown = createDrawableWithColor(70, 30, new Color(0.8f, 0.52f, 0.25f, 1));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(imageUp, imageDown, null, font);
        TextButton button = new TextButton(buttonName, buttonStyle);
        button.getLabel().setColor(new Color(0.31f, 0.31f, 0.46f, 1));

        table.add(button).padLeft(posX).padTop(posY).row();
    }

    public TextButton createButton(String buttonName) {
        BitmapFont font = resourceManager.pixel10Bold; // Ensure this font is correctly initialized in ResourceManager
        TextureRegionDrawable imageUp = createDrawableWithColor(70, 30, new Color(1, 0.65f, 0, 1));
        TextureRegionDrawable imageDown = createDrawableWithColor(70, 30, new Color(0.8f, 0.52f, 0.25f, 1));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(imageUp, imageDown, null, font);
        TextButton button = new TextButton(buttonName, buttonStyle);
        button.getLabel().setColor(new Color(0.31f, 0.31f, 0.46f, 1));
        return button;
    }


    protected abstract Table getScreenUI(final Skin skin);
    // Get the current screen
    //public ScreenType getScreenClass() {return ScreenType.getScreenTypeByClass(this.getClass());    }
    public ScreenType getScreenClass() {return ScreenType.getScreenTypeByClass(this.getClass());    }


    @Override
    public void addObserver(AudioObserver observer) {
        observers.add(observer);
    }
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
    @Override
    public void removeObserver(AudioObserver audioObserver) {    }
    @Override
    public void removeAllObservers() {    }
    @Override
    public void notify(AudioObserver.AudioCommand command, AudioObserver.AudioTypeEvent event) {
        for (AudioObserver observer : observers) {
            observer.onNotify(command, event);
        }
    }
}
