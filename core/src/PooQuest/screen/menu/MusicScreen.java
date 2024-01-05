package PooQuest.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.GaussianBlurEffect;
import PooQuest.audio.AudioManager;
import PooQuest.audio.AudioObserver;
import PooQuest.manager.ResourceManager;
import PooQuest.PooQuest;
import PooQuest.screen.ScreenType;


public class MusicScreen extends MenuScreen{
    private Table musicTable;
    private final Stage musicStage = new Stage();
    private final ScreenType previousScreen;
    private float stateTime;
    private VfxManager vfxManager;
    private GaussianBlurEffect vfxEffect;
    public MusicScreen(PooQuest context, ScreenType previousScreen, ResourceManager resourceManager) {

        super(context, resourceManager);
        this.previousScreen = previousScreen;

        resourceManager.setMusicScreen(true);
        loadContents();

    }
    private void loadContents() {
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        vfxEffect = new GaussianBlurEffect();
        musicTable = createTable();

        handleMusicSettings();
        handleMusicBackButton();
    }
    private void handleMusicSettings() {
        Label musicLabel = new Label("MUSIC", resourceManager.skin);
        musicLabel.setAlignment(Align.left);
        Slider musicSlider = new Slider(0, 1, 0.01f, false, resourceManager.skin);
        musicSlider.setValue(context.getPreferenceManager().getMusicVolume());
        musicSlider.addListener(event -> {
            context.getPreferenceManager().setMusicVolume(musicSlider.getValue());
            AudioManager.getInstance().getCurrentMusic().setVolume(context.getPreferenceManager().getMusicVolume());
            return false;

        });
        CheckBox musicCheckbox = new CheckBox("Enable Music", resourceManager.skin);
        musicCheckbox.setChecked(context.getPreferenceManager().isMusicEnabled());
        musicCheckbox.addListener(event -> {
            context.getPreferenceManager().setMusicEnabled(musicCheckbox.isChecked());
            notify(AudioObserver.AudioCommand.MUSIC_PLAY_LOOP, previousScreen.getMusicTheme());
            return false;
        });
        Label soundLabel = new Label("SOUND", resourceManager.skin);
        soundLabel.setAlignment(Align.left);
        Slider soundSlider = new Slider(0, 1, 0.01f, false, resourceManager.skin);
        soundSlider.setValue(context.getPreferenceManager().getSoundVolume());
        soundSlider.addListener(event -> {
            context.getPreferenceManager().setSoundVolume(soundSlider.getValue());
            return false;
        });
        CheckBox soundCheckbox = new CheckBox("Enable Sound", resourceManager.skin);
        soundCheckbox.setChecked(context.getPreferenceManager().isSoundEffectsEnabled());
        soundCheckbox.addListener(event -> {
            boolean enabled = soundCheckbox.isChecked();
            context.getPreferenceManager().setSoundEffectsEnabled(enabled);
            return false;
        });

        musicTable.row();
        musicTable.add(musicLabel).padLeft(0).padTop(musicTable.getHeight()/20);
        musicTable.row();
        musicTable.add(musicSlider).padLeft(0).padTop(musicTable.getHeight()/20);
        musicTable.row();
        musicTable.add(musicCheckbox).padLeft(0).padTop(musicTable.getHeight()/20);
        musicTable.row();
        musicTable.add(soundLabel).padLeft(0).padTop(musicTable.getHeight()/20);
        musicTable.row();
        musicTable.add(soundSlider).padLeft(0).padTop(musicTable.getHeight()/20);
        musicTable.row();
        musicTable.add(soundCheckbox).padLeft(0).padTop(musicTable.getHeight()/20);
        musicTable.row();

    }

    private void handleMusicBackButton() {
        addButton("Back", 0, musicTable.getHeight()/20, musicTable);

        Actor backButton = musicTable.getCells().get(6).getActor();
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                context.setScreen(previousScreen);
            }
        });
    }


    @Override
    protected Table getScreenUI(Skin skin) {
        return null;
    }

    @Override
    public void show() {
        musicStage.addActor(musicTable);
        Gdx.input.setInputProcessor(musicStage);
    }

    @Override
    public void render(float delta) {
        stateTime += Gdx.graphics.getDeltaTime();

        vfxManager.cleanUpBuffers();

        // Début de la capture d'entrée pour les effets VFX
        vfxManager.beginInputCapture();


        // Mise à jour et rendu de l'étape de musique
        clearAndRenderBackground();
        musicStage.act(delta);
        musicStage.draw();

        // Fin de la capture d'entrée
        vfxManager.endInputCapture();

        // Application des effets VFX et rendu à l'écran
        vfxManager.applyEffects();
        vfxManager.renderToScreen();
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
        musicTable.remove();
        vfxManager.dispose();
        vfxEffect.dispose();
    }

    @Override
    public void hide() {
        resourceManager.setMusicScreen(false);
    }

    @Override
    public void resize(int width, int height) {
        //Nothing
    }
}
