package com.myrpg.game.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceManager.class);

    protected boolean isMusicScreen;
    protected boolean isMenuNewGameScreen;
    protected boolean isMenuLoadGameScreen;
    private static InternalFileHandleResolver filePathResolver =  new InternalFileHandleResolver();

    // MAP
    public final static int SQUARE_TILE_SIZE = 32;

    // ATLAS
    public TextureAtlas atlas;



    // IMAGES
    public Texture backgroundSheet;
    public Texture battleBackgroundMeadow;
    public Pixmap cursor;

    // BUTTON
    public TextureRegion[][] button;

    // FONT
    public BitmapFont pixel10;

    // SETTINGS
    public static Skin skin;

    // ENTITIES
    public Texture heroWalkUp;
    public BitmapFont pixel10Bold;


    private static AssetManager assetManager = new AssetManager();

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


    public static boolean isAssetLoaded(String fileName) {
        return assetManager.isLoaded(fileName);
    }

    public static TiledMap getMapAsset(String mapFilenamePath) {
        TiledMap map = null;

        // once the asset manager is done loading
        if (assetManager.isLoaded(mapFilenamePath)) {
            map = assetManager.get(mapFilenamePath, TiledMap.class);
        } else {
            LOGGER.debug("Map is not loaded: {}", mapFilenamePath);
        }

        return map;
    }

    public static void loadTextureAsset(String textureFilenamePath) {
        if (textureFilenamePath == null || textureFilenamePath.isEmpty()) {
            return;
        }

        if (assetManager.isLoaded(textureFilenamePath)) {
            return;
        }

        //load asset
        if (filePathResolver.resolve(textureFilenamePath).exists()) {
            assetManager.setLoader(Texture.class, new TextureLoader(filePathResolver));
            assetManager.load(textureFilenamePath, Texture.class);
            //Until we add loading screen, just block until we load the map
            assetManager.finishLoadingAsset(textureFilenamePath);
        } else {
            LOGGER.debug("Texture doesn't exist!: {}", textureFilenamePath);
        }
    }

    public static Texture getTextureAsset(String textureFilenamePath) {
        Texture texture = null;

        // once the asset manager is done loading
        if (assetManager.isLoaded(textureFilenamePath)) {
            texture = assetManager.get(textureFilenamePath,Texture.class);
        } else {
            LOGGER.debug("Texture is not loaded: {}", textureFilenamePath);
        }

        return texture;
    }

    public static void loadMusicAsset(String musicFilenamePath) {
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

    public static Music getMusicAsset(String musicFilenamePath) {
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
}
