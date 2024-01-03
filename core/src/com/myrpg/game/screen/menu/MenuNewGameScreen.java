package com.myrpg.game.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

public class MenuNewGameScreen extends MenuScreen {
    private Table mainTable;
    private Stage mainStage = new Stage();
    private TextField profileText;
    private Dialog overwriteDialog;
    private ScreenType previousScreen;
    private Texture warriorTexture;
    private final String warriorTexturePath = "sprites/characters/warrior-fists.png";
    private Texture mageTexture;
    private final String mageTexturePath = "sprites/characters/mage-fists.png";
    private Texture thiefTexture;
    private final String thiefTexturePath = "sprites/characters/thief-fists.png";
    private final int FRAME_WIDTH = 64;
    private final int FRAME_HEIGHT = 64;
    private final int FRAME_COLS = 9;
    private final int DEFAULT_FRAME_X = 10;
    private TextureRegion currentCharacterFrame;
    private float characterFrametime = 0f;
    private float stateTime;
    private Image characterImage;
    private final TextureRegionDrawable currentCharacterDrawable;

    private enum CharacterClass {
        WARRIOR, MAGE, THIEF
    }

    private CharacterClass selectedClass = CharacterClass.WARRIOR;
    private Animation<TextureRegion> mageWalkAnimation;
    private Animation<TextureRegion> thiefWalkAnimation;
    private Animation<TextureRegion> warriorWalkAnimation;

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
        ResourceManager.loadTextureAsset(warriorTexturePath);
        ResourceManager.loadTextureAsset(mageTexturePath);
        ResourceManager.loadTextureAsset(thiefTexturePath);

        warriorTexture = ResourceManager.getTextureAsset(warriorTexturePath);
        mageTexture = ResourceManager.getTextureAsset(mageTexturePath);
        thiefTexture = ResourceManager.getTextureAsset(thiefTexturePath);

        TextureRegion[][] warriorTextureFrames = TextureRegion.split(warriorTexture, FRAME_WIDTH, FRAME_HEIGHT);
        TextureRegion[][] mageTextureFrames = TextureRegion.split(mageTexture, FRAME_WIDTH, FRAME_HEIGHT);
        TextureRegion[][] thiefTextureFrames = TextureRegion.split(thiefTexture, FRAME_WIDTH, FRAME_HEIGHT);

        Array<TextureRegion> mageWalkFrames = new Array<>(9);
        Array<TextureRegion> thiefWalkFrames = new Array<>(9);
        Array<TextureRegion> warriorWalkFrames = new Array<>(9);

        for (int i = 0; i < FRAME_COLS; i++) {
            warriorWalkFrames.add(warriorTextureFrames[DEFAULT_FRAME_X][i]);
            mageWalkFrames.add(mageTextureFrames[DEFAULT_FRAME_X][i]);
            thiefWalkFrames.add(thiefTextureFrames[DEFAULT_FRAME_X][i]);
        }

        warriorWalkAnimation = new Animation<>(0.25f, warriorWalkFrames, Animation.PlayMode.LOOP);
        mageWalkAnimation = new Animation<>(0.25f, mageWalkFrames, Animation.PlayMode.LOOP);
        thiefWalkAnimation = new Animation<>(0.25f, thiefWalkFrames, Animation.PlayMode.LOOP);
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
        Label profileName = new Label("Enter Profile Name: ", ResourceManager.skin);
        profileText = new TextField("default", ResourceManager.skin);
        profileText.setMaxLength(20);
        mainTable.add(profileName).center().colspan(2).row();
        mainTable.add(profileText).center().colspan(2).padBottom(10).row();
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
                        selectedClass = CharacterClass.WARRIOR;
                        break;
                    case "Mage":
                        selectedClass = CharacterClass.MAGE;
                        break;
                    case "Thief":
                        selectedClass = CharacterClass.THIEF;
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
                String messageText = profileText.getText();
                // check to see if the current profile matches one that already exists
                boolean exists = ProfileManager.getInstance().doesProfileExist(messageText);
                if (exists) {
                    overwriteDialog.show(mainStage); // Pop-up dialog for Overwrite
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
                String messageText = profileText.getText();
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
        stateTime += Gdx.graphics.getDeltaTime();

        clearAndRenderBackground();

        mainStage.act(delta);
        setCurrentCharacterDrawable(delta);
        mainStage.draw();
    }

    private void setCurrentCharacterDrawable(float delta) {
        characterFrametime = (characterFrametime + delta) % FRAME_COLS;
        switch (selectedClass) {
            case WARRIOR:
                currentCharacterFrame = warriorWalkAnimation.getKeyFrame(characterFrametime);
                break;
            case MAGE:
                currentCharacterFrame = mageWalkAnimation.getKeyFrame(characterFrametime);
                break;
            case THIEF:
                currentCharacterFrame = thiefWalkAnimation.getKeyFrame(characterFrametime);
                break;
        }
        currentCharacterDrawable.setRegion(currentCharacterFrame);
        characterImage.setDrawable(currentCharacterDrawable);
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
        mainTable.remove();
    }

    @Override
    public void hide() {
        resourceManager.setMenuNewGameScreen(true);
    }

    @Override
    public void resize(int width, int height) {
    }
}
