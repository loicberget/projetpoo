package PooQuest.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceManager {
    private static ResourceManager instance = null;
    private final Logger LOGGER = LoggerFactory.getLogger(ResourceManager.class);
    private final String TAG = ResourceManager.class.getSimpleName();
    protected boolean isMusicScreen;
    protected boolean isMenuNewGameScreen;
    protected boolean isMenuLoadGameScreen;
    private InternalFileHandleResolver filePathResolver =  new InternalFileHandleResolver();
    public TextureAtlas atlas;
    // IMAGES
    public Texture backgroundSheet;
    // SETTINGS
    public Skin skin;
    public BitmapFont pixel10Bold;
    private AssetManager assetManager = new AssetManager();

    public ResourceManager() {

        // IMAGES
        assetManager.load("background/natureBackground_frames_sheet.png", Texture.class);

        assetManager.finishLoading();

        // IMAGES
        backgroundSheet = assetManager.get("background/natureBackground_frames_sheet.png");

        // FONT

        pixel10Bold = new BitmapFont(Gdx.files.internal("fonts/default.fnt"), false);

        // SETTINGS
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
    }

    public static ResourceManager getInstance() {
        if (instance==null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public boolean isMusicScreen() {
        return isMusicScreen;
    }

    public void setMusicScreen(boolean musicScreen) {
        isMusicScreen = musicScreen;
    }

    public boolean isMenuNewGameScreen() {
        return isMenuNewGameScreen;
    }

    public void setMenuNewGameScreen(boolean menuNewGameScreen) {
        isMenuNewGameScreen = menuNewGameScreen;
    }

    public boolean isMenuLoadGameScreen() {
        return isMenuLoadGameScreen;
    }

    public void setMenuLoadGameScreen(boolean menuLoadGameScreen) {
        isMenuLoadGameScreen = menuLoadGameScreen;
    }


    public boolean isAssetLoaded(String fileName) {
        return assetManager.isLoaded(fileName);
    }



    public void loadMusicAsset(String musicFilenamePath) {
        if (musicFilenamePath == null || musicFilenamePath.isEmpty()) {
            return;
        }
        if (assetManager.isLoaded(musicFilenamePath)) {
            return;
        }

        //load asset
        if (filePathResolver.resolve(musicFilenamePath).exists()) {
            assetManager.setLoader(Music.class, new MusicLoader(filePathResolver));
            assetManager.load(musicFilenamePath, Music.class);
            //Until we add loading screen, just block until we load the map
            assetManager.finishLoadingAsset(musicFilenamePath);
            LOGGER.debug("Music loaded!: {}", musicFilenamePath);
        } else {
            LOGGER.debug("Music doesn't exist!: {}", musicFilenamePath);
        }
    }

    public Music getMusicAsset(String musicFilenamePath) {
        Music music = null;

        // once the asset manager is done loading
        if (assetManager.isLoaded(musicFilenamePath)) {
            music = assetManager.get(musicFilenamePath, Music.class);
        } else {
            LOGGER.debug("Music is not loaded: {}", musicFilenamePath);
        }

        return music;
    }

    public void dispose() {
        assetManager.dispose();
        if (this.atlas != null) {
            this.atlas.dispose();
        }
        backgroundSheet.dispose();
    }

    public void loadTextureAsset(String textureFilenamePath){
        if( textureFilenamePath == null || textureFilenamePath.isEmpty() ){
            return;
        }

        if(assetManager.isLoaded(textureFilenamePath) ){
            return;
        }

        //load asset
        if( filePathResolver.resolve(textureFilenamePath).exists() ){
            assetManager.setLoader(Texture.class, new TextureLoader(filePathResolver));
            assetManager.load(textureFilenamePath, Texture.class);
            //Until we add loading screen, just block until we load the map
            assetManager.finishLoadingAsset(textureFilenamePath);
        }
        else{
            Gdx.app.debug(TAG, "Texture doesn't exist!: " + textureFilenamePath );
        }
    }

    public Texture getTextureAsset(String textureFilenamePath){
        Texture texture = null;

        // once the asset manager is done loading
        if( assetManager.isLoaded(textureFilenamePath) ){
            texture = assetManager.get(textureFilenamePath,Texture.class);
        } else {
            Gdx.app.debug(TAG, "Texture is not loaded: " + textureFilenamePath );
        }

        return texture;
    }

    public void unloadAsset(String assetFilenamePath){
        // once the asset manager is done loading
        if( assetManager.isLoaded(assetFilenamePath) ){
            assetManager.unload(assetFilenamePath);
        } else {
            Gdx.app.debug(TAG, "Asset is not loaded; Nothing to unload: " + assetFilenamePath );
        }
    }
}
