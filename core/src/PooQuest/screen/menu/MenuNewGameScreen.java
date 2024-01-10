package PooQuest.screen.menu;

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
import PooQuest.manager.ResourceManager;
import PooQuest.profile.ProfileManager;
import PooQuest.PooQuest;
import PooQuest.screen.ScreenType;
import PooQuest.utilities.Utilities;

import java.util.HashMap;

import static PooQuest.audio.AudioObserver.AudioTypeEvent.MENU_THEME;

public class MenuNewGameScreen extends MenuScreen {
    private Table mainTable;
    private Stage mainStage = new Stage();
    private TextField profileField;
    private Dialog overwriteDialog;
    private ScreenType previousScreen;
    private static final String warriorTexturePath = "sprites/characters/warrior-fists.png";
    private static final String mageTexturePath = "sprites/characters/mage-fists.png";
    private static final String thiefTexturePath = "sprites/characters/thief-fists.png";
    private final int FRAME_WIDTH = 64;
    private final int FRAME_HEIGHT = 64;
    private final int FRAME_COLS = 9;
    private final int WALK_DOWN_ROW = 10;
    private TextureRegion currentCharacterFrame;
    private float characterFrametime = 0f;
    private Image characterImage;
    private final TextureRegionDrawable currentCharacterDrawable;
    private final String WARRIOR = "Warrior";
    private final String MAGE = "Mage";
    private final String THIEF = "Thief";
    private String selectedClass = WARRIOR;
    private final HashMap<String, Animation<TextureRegion>> walkAnimationMap = new HashMap<>();
    SelectBox<String> classSelectBox = new SelectBox<>(resourceManager.skin);

    public MenuNewGameScreen(PooQuest context, ScreenType previousScreen, ResourceManager resourceManager) {
        super(context, resourceManager);
        this.previousScreen = previousScreen;
        super.musicTheme = MENU_THEME;

        resourceManager.setMenuNewGameScreen(true);

        currentCharacterDrawable = new TextureRegionDrawable();

        loadTexturesAndAnimations();

        createMainTable();

        createOverwriteDialog();

    }

    public static void loadMenuAssets() {
        ResourceManager r = ResourceManager.getInstance();
        r.loadTextureAsset(warriorTexturePath);
        r.loadTextureAsset(mageTexturePath);
        r.loadTextureAsset(thiefTexturePath);
    }

    private void loadTexturesAndAnimations() {
        walkAnimationMap.put(WARRIOR, Utilities.createAnimationFromTx(warriorTexturePath, WALK_DOWN_ROW, FRAME_COLS, FRAME_WIDTH, FRAME_HEIGHT));
        walkAnimationMap.put(MAGE, Utilities.createAnimationFromTx(mageTexturePath, WALK_DOWN_ROW, FRAME_COLS, FRAME_WIDTH, FRAME_HEIGHT));
        walkAnimationMap.put(THIEF, Utilities.createAnimationFromTx(thiefTexturePath, WALK_DOWN_ROW, FRAME_COLS, FRAME_WIDTH, FRAME_HEIGHT));
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
        Label profileLabel = new Label("Enter Profile Name: ", resourceManager.skin);
        profileField = new TextField("", resourceManager.skin);
        profileField.setMaxLength(20);
        profileField.setMessageText("Your name");
        mainTable.add(profileLabel).center().colspan(2).row();
        mainTable.add(profileField).center().colspan(2).padBottom(10).row();
    }

    private void createClassSelectBox() {
        Label classLabel = new Label("Select Class: ", resourceManager.skin);

        characterImage = new Image(currentCharacterFrame);

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
                String profileName = profileField.getText();
                boolean exists = ProfileManager.getInstance().doesProfileExist(profileName);
                if (exists) {
                    overwriteDialog.show(mainStage);
                } else {
                    ProfileManager.getInstance().writeProfileToStorage(profileName, "", false);
                    ProfileManager.getInstance().setProperty("playerClass", classSelectBox.getSelected());
                    ProfileManager.getInstance().setCurrentProfile(profileName);
                    ProfileManager.getInstance().setIsNewProfile(true);
                    ProfileManager.getInstance().saveProfile();
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
        overwriteDialog = new Dialog("Overwrite?", resourceManager.skin);
        Label overwriteLabel = new Label("Overwrite existing profile name?", resourceManager.skin);

        overwriteDialog.setKeepWithinStage(true);
        overwriteDialog.setModal(true);
        overwriteDialog.setMovable(false);
        overwriteDialog.text(overwriteLabel);
        overwriteDialog.row();

        TextButton overwriteButton = new TextButton("Overwrite", resourceManager.skin);
        overwriteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String profileName = profileField.getText();
                ProfileManager.getInstance().writeProfileToStorage(profileName, "", true);
                ProfileManager.getInstance().setCurrentProfile(profileName);
                ProfileManager.getInstance().setIsNewProfile(true);
                ProfileManager.getInstance().setProperty("playerClass", classSelectBox.getSelected());
                ProfileManager.getInstance().saveProfile();
                overwriteDialog.hide();
                context.setScreen(ScreenType.GAME);
            }
        });
        overwriteDialog.button(overwriteButton).bottom().left();

        TextButton cancelButton = new TextButton("Cancel", resourceManager.skin);
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
        currentCharacterFrame = walkAnimationMap.get(selectedClass).getKeyFrame(characterFrametime);
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
