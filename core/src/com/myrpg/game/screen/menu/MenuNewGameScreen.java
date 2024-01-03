package com.myrpg.game.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.myrpg.game.manager.ResourceManager;
import com.myrpg.game.profile.ProfileManager;
import com.myrpg.game.rpg_game;
import com.myrpg.game.screen.ScreenType;

import static com.myrpg.game.audio.AudioObserver.AudioTypeEvent.MENU_THEME;
import static com.myrpg.game.utilities.Utilities.createAnimationFromTx;

public class MenuNewGameScreen extends MenuScreen {
    private Table mainTable;
    private Stage mainStage = new Stage();
    private TextField profileField;
    private Dialog overwriteDialog;
    private ScreenType previousScreen;
    private final static String warriorTexturePath = "sprites/characters/warrior-fists.png";
    private final static String mageTexturePath = "sprites/characters/mage-fists.png";
    private static final String thiefTexturePath = "sprites/characters/thief-fists.png";
    private static final int FRAME_WIDTH = 64;
    private static final int FRAME_HEIGHT = 64;
    private static final int FRAME_COLS = 9;
    private static final int WALK_DOWN_ROW = 10;
    private TextureRegion currentCharacterFrame;
    private float characterFrametime = 0f;
    private Image characterImage;
    private final TextureRegionDrawable currentCharacterDrawable;
    private Array<Animation<TextureRegion>> walkAnimation;

    private static int WARRIOR = 0;
    private static int MAGE = 1;
    private static int THIEF = 2;

    private static int selectedClass = WARRIOR;

    public MenuNewGameScreen(rpg_game context, ScreenType previousScreen, ResourceManager resourceManager) {
        super(context, resourceManager);
        this.previousScreen = previousScreen;
        super.musicTheme = MENU_THEME;


        resourceManager.setMenuNewGameScreen(true);

        currentCharacterDrawable = new TextureRegionDrawable();
        loadTexturesAndAnimations();

        createMainTable();

        createOverwriteDialog();

    }

    private void loadTexturesAndAnimations() {
        walkAnimation = new Array<>();
        walkAnimation.insert(WARRIOR, createAnimationFromTx(warriorTexturePath, WALK_DOWN_ROW, FRAME_COLS, FRAME_WIDTH, FRAME_HEIGHT));
        walkAnimation.insert(MAGE, createAnimationFromTx(mageTexturePath, WALK_DOWN_ROW, FRAME_COLS, FRAME_WIDTH, FRAME_HEIGHT));
        walkAnimation.insert(THIEF, createAnimationFromTx(thiefTexturePath, WALK_DOWN_ROW, FRAME_COLS, FRAME_WIDTH, FRAME_HEIGHT));
    }

    private void createMainTable() {
        mainTable = createTable();
        mainTable.setFillParent(true);
        mainTable.center();

        createProfileTextField();

        createClassSelectBox();

        createButtons();
    }

    private void createProfileTextField() {
        Label profileLabel = new Label("Enter Profile Name: ", ResourceManager.skin);
        profileField = new TextField("", ResourceManager.skin);
        profileField.setMaxLength(20);
        profileField.setMessageText("Your name");
        mainTable.add(profileLabel).center().colspan(2).row();
        mainTable.add(profileField).center().colspan(2).padBottom(10).row();
    }

    private void createClassSelectBox() {
        Label classLabel = new Label("Select Class: ", ResourceManager.skin);

        characterImage = new Image(currentCharacterFrame);

        SelectBox<String> classSelectBox = new SelectBox<>(ResourceManager.skin);
        classSelectBox.setItems("Warrior", "Mage", "Thief");

        mainTable.add(classLabel).center();
        mainTable.add(classSelectBox).center().padBottom(10).row();
        mainTable.add(characterImage).center().colspan(2).padBottom(10).row();

        classSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switch (classSelectBox.getSelected()) {
                    case "Warrior":
                        selectedClass = WARRIOR;
                        break;
                    case "Mage":
                        selectedClass = MAGE;
                        break;
                    case "Thief":
                        selectedClass = THIEF;
                        break;
                }
            }
        });
    }

    private void createButtons() {
        TextButton playButton = createButton("Play");
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent even, float x, float y) {
                String messageText = profileField.getText();
                boolean exists = ProfileManager.getInstance().doesProfileExist(messageText);
                if (exists) {
                    overwriteDialog.show(mainStage);
                } else {
                    ProfileManager.getInstance().writeProfileToStorage(messageText, "", false);
                    ProfileManager.getInstance().setCurrentProfile(messageText);
                    ProfileManager.getInstance().setIsNewProfile(true);
                    context.setScreen(ScreenType.GAME);
                }
            }
        });
        mainTable.add(playButton).left();

        TextButton backButton = createButton("Back");
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent even, float x, float y) {
                context.setScreen(previousScreen);
            }
        });
        mainTable.add(backButton).right();
    }

    private void createOverwriteDialog() {
        overwriteDialog = new Dialog("Overwrite?", ResourceManager.skin);
        Label overwriteLabel = new Label("Overwrite existing profile name?", ResourceManager.skin);

        overwriteDialog.setKeepWithinStage(true);
        overwriteDialog.setModal(true);
        overwriteDialog.setMovable(false);
        overwriteDialog.text(overwriteLabel);
        overwriteDialog.row();

        TextButton overwriteButton = new TextButton("Overwrite", ResourceManager.skin);
        overwriteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String messageText = profileField.getText();
                ProfileManager.getInstance().writeProfileToStorage(messageText, "", true);
                ProfileManager.getInstance().setCurrentProfile(messageText);
                ProfileManager.getInstance().setIsNewProfile(true);
                overwriteDialog.hide();
                context.setScreen(ScreenType.GAME);
            }
        });
        overwriteDialog.button(overwriteButton).bottom().left();

        TextButton cancelButton = new TextButton("Cancel", ResourceManager.skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overwriteDialog.hide();
            }
        });
        overwriteDialog.button(cancelButton).bottom().right();
    }

    @Override
    protected Table getScreenUI(Skin skin) {
        return null;
    }

    @Override
    public void show() {
        mainStage.addActor(mainTable);
        Gdx.input.setInputProcessor(mainStage);
    }

    @Override
    public void render(float delta) {
        clearAndRenderBackground();
        mainStage.act(delta);
        setCurrentCharacterDrawable(delta);
        mainStage.draw();
    }

    private void setCurrentCharacterDrawable(float delta) {
        characterFrametime = (characterFrametime + delta) % FRAME_COLS;
        currentCharacterFrame = walkAnimation.get(selectedClass).getKeyFrame(characterFrametime);
        currentCharacterDrawable.setRegion(currentCharacterFrame);
        characterImage.setDrawable(currentCharacterDrawable);
    }

    @Override
    public void dispose() {
        super.dispose();
        mainTable.remove();
    }

    @Override
    public void hide() {
        resourceManager.setMenuNewGameScreen(true);
    }

}
