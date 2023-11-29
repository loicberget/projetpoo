package com.myrpg.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.myrpg.game.manager.PreferenceManager;
import com.myrpg.game.manager.ResourceManager;
import com.myrpg.game.screen.AbstractScreen;
import com.myrpg.game.screen.GameScreen;
import com.myrpg.game.screen.LoadingScreen;
import com.myrpg.game.screen.ScreenType;
import com.myrpg.game.screen.menu.MenuLoadGameScreen;
import com.myrpg.game.screen.menu.MenuNewGameScreen;
import com.myrpg.game.screen.menu.MenuScreen;
import com.myrpg.game.screen.menu.MusicScreen;

import java.util.EnumMap;

public class rpg_game extends Game {
	private static final String TAG = rpg_game.class.getSimpleName();
	private EnumMap<ScreenType, AbstractScreen> screenCache;
	private OrthographicCamera gameCamera;
	private SpriteBatch spriteBatch;
	private ResourceManager resourceManager;
	private PreferenceManager preferenceManager;
	private World world;
	private WorldContactListener worldContactListener;
	private Box2DDebugRenderer box2DDebugRenderer;
	private float accumulator;
	private static final float FIXED_TIME_STEP = 1 / 60f;
	public static final float UNIT_SCALE = 1/32f;
	public static final short BIT_PLAYER = 1<<0; // 0001
	public static final short BIT_GROUND = 1<<1; // 0010
	private AssetManager assetManager;
	private Stage stage;
	private Skin skin;
	private FitViewport screenViewport;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		accumulator = 0;

		spriteBatch = new SpriteBatch();

		// Box2D initialization
		Box2D.init();
		world = new World(new Vector2(0, 0), true); // Initialized the gravity in our game
		worldContactListener = new WorldContactListener();
		world.setContactListener(worldContactListener);
		box2DDebugRenderer = new Box2DDebugRenderer();

		// Assets initialization
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(assetManager.getFileHandleResolver()));
		initializeSkin();
		stage = new Stage(new FitViewport(800, 300), spriteBatch);

		// Screen initialization
		gameCamera = new OrthographicCamera();
		screenViewport = new FitViewport(24, 9, gameCamera); // I might need to change that to adapt it for a desktop game
		screenCache = new EnumMap<ScreenType, AbstractScreen>(ScreenType.class); // Initialized the screen cache
		setScreen(ScreenType.LOADING); // Starting on the loading screen

		resourceManager = new ResourceManager();
		preferenceManager = PreferenceManager.getInstance();

	}

	private void initializeSkin() {
		// generate the ttf bitmaps
		final ObjectMap<String, Object> resources = new ObjectMap<String, Object>();
		final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/font.ttf"));
		final FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.minFilter = Texture.TextureFilter.Linear;
		fontParameter.magFilter	= Texture.TextureFilter.Linear;
		final int[] sizesToCreate = {16, 20, 26, 32};
		for(final int size : sizesToCreate) {
			fontParameter.size = size;
			resources.put("font_" + size, fontGenerator.generateFont(fontParameter)); // returns a bitmap fonts
		}
		fontGenerator.dispose();

		// load skin
		final SkinLoader.SkinParameter skinParameter= new SkinLoader.SkinParameter("ui/hud.atlas", resources);
		assetManager.load("ui/hud.json", Skin.class, skinParameter);
		assetManager.finishLoading();
		skin = assetManager.get("ui/hud.json", Skin.class);

	}

	// Getters
	public Skin getSkin() { return skin; }
	public Stage getStage() { return stage; }
	public SpriteBatch getSpriteBatch() { return spriteBatch; }
	public AssetManager getAssetManager() { return assetManager; }
	public OrthographicCamera getGameCamera() { return gameCamera;}
	public FitViewport getScreenViewport () { return screenViewport;}
	public World getWorld() { return world;	}
	public Box2DDebugRenderer getBox2DDebugRenderer() {
		return box2DDebugRenderer;
	}
	public PreferenceManager getPreferenceManager() {
		return preferenceManager;
	}


	// Switching between screens without overcharged the memory
	public void setScreen(final ScreenType screenType) {
		final Screen screen = screenCache.get(screenType); // gets what's in the screen cache
		if (screen == null) {
			// if screen is not created yet  -> create it
			Gdx.app.debug(TAG, "Creating new screen: " + screenType);
			switch (screenType){
				case GAME:
					screenCache.put(screenType, new GameScreen(this));
					setScreen(screenType.GAME);
					break;
				case MENU:
					screenCache.put(screenType, new MenuScreen(this, resourceManager));
					setScreen(screenType.MENU);
					break;
				case MENU_NEW_GAME:
					screenCache.put(screenType, new MenuNewGameScreen(this, screenType.MENU, resourceManager));
					setScreen(screenType.MENU_NEW_GAME);
					break;
				case MENU_LOAD_GAME:
					screenCache.put(screenType, new MenuLoadGameScreen(this, screenType.MENU, resourceManager));
					setScreen(screenType.MENU_LOAD_GAME);
					break;
				case LOADING:
					screenCache.put(screenType, new LoadingScreen(this));
					setScreen(screenType.LOADING);
					break;
				case MUSIC:
					screenCache.put(screenType, new MusicScreen(this, screenType.MENU, resourceManager));
					setScreen(screenType.MUSIC);
					break;
				default:
					throw new GdxRuntimeException("Screen " + screenType + " is not recognized");
			}
			//final AbstractScreen newScreen = (AbstractScreen) ClassReflection.getConstructor(screenType.getScreenClass(), rpg_game.class).newInstance(this);
			//screenCache.put(screenType, newScreen); // Put the newScreen in the cache so that it doesn't create another one next time
			// setScreen (newScreen);
		} else {
			Gdx.app.debug(TAG, "Switching to new screen " + screenType);
			setScreen(screen);
		}
	}

	@Override
	public void render() {
		super.render();

		// Always the same speed for objects
		accumulator += Math.min(0.25f, Gdx.graphics.getDeltaTime());
		while(accumulator >= FIXED_TIME_STEP) {
			world.step(FIXED_TIME_STEP, 6, 2);
			accumulator -=  FIXED_TIME_STEP;
		}

		// final float alpha = accumulator / FIXED_TIME_STEP;
		stage.getViewport().apply();
		stage.act();
		stage.draw();
	}

	@Override
	public void dispose() {
		super.dispose();
		box2DDebugRenderer.dispose();
		world.dispose();
		assetManager.dispose();
		spriteBatch.dispose();
		stage.dispose();
	}
}
