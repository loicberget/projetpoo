package com.myrpg.game.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.myrpg.game.manager.ResourceManager;
import com.myrpg.game.profile.ProfileManager;
import com.myrpg.game.rpg_game;
import com.myrpg.game.screen.ScreenType;

import java.util.ArrayList;

import static com.myrpg.game.audio.AudioObserver.AudioTypeEvent.MENU_THEME;


public class MenuLoadGameScreen extends MenuScreen {

    // UI elements for the load game menu
    private Table loadTable, topTable, bottomTable;
    private Stage loadStage = new Stage();
    private ScreenType currentScreen = getScreenClass();
    private ScreenType previousScreen;
    private List<String> listItems; // List to display saved profiles
    private float stateTime; // Time state for rendering

    // Constructor for MenuLoadGameScreen
    public MenuLoadGameScreen(rpg_game gdxGame, ScreenType previousScreen, ResourceManager resourceManager) {
        super(gdxGame, resourceManager);
        this.previousScreen = previousScreen;
        super.musicTheme = MENU_THEME; // Set the music theme

        resourceManager.setMenuLoadGameScreen(true);

        // Setup for UI tables
        loadTable = createTable();
        topTable = setupTopTable();
        bottomTable = setupBottomTable();

        createProfileList(); // Create list of profiles
        handleLoadButton(); // Handle 'Play' button click
        handleLoadBackButton(); // Handle 'Back' button click
    }

    private Table setupTopTable() {
        Table table = createTable();
        table.center();
        table.setFillParent(true);
        return table;
    }

    private Table setupBottomTable() {
        Table table = createTable();
        table.setWidth(Gdx.graphics.getWidth());
        table.setHeight(Gdx.graphics.getHeight() / 2f);
        table.center();
        return table;
    }

    // Method to create and populate the profile list
    private void createProfileList() {
        ProfileManager.getInstance().storeAllProfiles();
        listItems = new List<>(ResourceManager.skin);
        Array<String> profiles = ProfileManager.getInstance().getProfileList();
        listItems.setItems(profiles);
        ScrollPane scrollPane = configureScrollPane(listItems);
        topTable.add(scrollPane).center();
    }

    // Configure the scroll pane for the list
    private ScrollPane configureScrollPane(List<String> list) {
        ScrollPane scrollPane = new ScrollPane(list);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(true);
        return scrollPane;
    }

    // Method to handle the 'Play' button functionality
    private void handleLoadButton() {
        createButton("Play", 0, loadTable.getHeight() / 9, loadTable);
        Actor loadButton = loadTable.getCells().get(0).getActor();
        topTable.padBottom(loadButton.getHeight());
        bottomTable.add(loadButton).padRight(120).padBottom(50);

        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreenWithTransition(getScreenClass(), ScreenType.GAME, new ArrayList<>());
                //loadProfileAction();
            }
        });
    }

    // Method to handle the 'Back' button functionality
    private void handleLoadBackButton() {
        createButton("Back", 0, loadTable.getHeight() / 5, loadTable);
        Actor backButton = loadTable.getCells().get(1).getActor();
        bottomTable.add(backButton).padBottom(50);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreenWithTransition(getScreenClass(), previousScreen, new ArrayList<>());
            }
        });
    }
    /* TODO : See if that method is really useful
    // Action to perform when 'Play' button is clicked
    private void loadProfileAction() {
        previousScreen.dispose();
        if (listItems.getSelected() == null) return;

        String fileName = listItems.getSelected();
        if (fileName != null && !fileName.isEmpty()) {
            FileHandle file = ProfileManager.getInstance().getProfileFile(fileName);
            if (file != null) {
                ProfileManager.getInstance().setCurrentProfile(fileName);
                ProfileManager.getInstance().loadProfile();
                gdxGame.setGameScreen(new GameScreen(gdxGame, resourceManager));
                ArrayList<TransitionEffect> effects = new ArrayList<>();
                effects.add(new FadeOutTransitionEffect(1f));
                setScreenWithTransition((BaseScreen) gdxGame.getScreen(), gdxGame.getGameScreen(), effects);
            }
        }
    }*/

    @Override
    protected Table getScreenUI(Skin skin) {
        return null;
    }

    @Override
    public void show() {
        loadStage.addActor(loadTable);
        loadStage.addActor(topTable);
        loadStage.addActor(bottomTable);
        Gdx.input.setInputProcessor(loadStage);
    }

    @Override
    public void render(float delta) {
        stateTime += Gdx.graphics.getDeltaTime();

        ClearAndRenderBackground();

        show();
        loadStage.act(delta);
        loadStage.draw();
    }

    @Override
    public void resize(int width, int height) {
//        super.resize(width, height);
//        loadStage.getViewport().update(width, height, true);
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
        loadTable.remove();
        topTable.remove();
        bottomTable.remove();
    }

    @Override
    public void hide() {
        resourceManager.setMenuLoadGameScreen(false);
    }
}
