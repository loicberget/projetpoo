package PooQuest.screen;

import PooQuest.screen.menu.MenuNewGameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import PooQuest.manager.ResourceManager;
import PooQuest.map.Map;
import PooQuest.PooQuest;
import PooQuest.ui.LoadingUI;

import java.awt.*;

public class LoadingScreen extends AbstractScreen {
    private static final ResourceManager resourceManager = null;
    private final AssetManager assetManager;
    public static final String TAG = Map.class.getSimpleName();
    public LoadingScreen(final PooQuest context) {
        super(context, resourceManager);

        this.assetManager = context.getAssetManager();
        assetManager.load("map/map.tmx", TiledMap.class);

        MenuNewGameScreen.loadMenuAssets();
    }

    @Override
    protected Table getScreenUI(final Skin skin) {
        Gdx.app.debug(TAG, "Loading screen UI");
        return new LoadingUI(stage, skin);
    }

    @Override
    public void render(final float delta) {
        // color of the loading screen
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ((LoadingUI)screenUI).setProgress(assetManager.getProgress());
        // once the asset manager is done loading -> change the screen
        if(assetManager.update()){
            context.setScreen(ScreenType.MENU);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
}
