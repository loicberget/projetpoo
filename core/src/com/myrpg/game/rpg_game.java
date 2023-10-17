package com.myrpg.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.myrpg.game.screen.AbstractScreen;
import com.myrpg.game.screen.LoadingScreen;
import com.myrpg.game.screen.ScreenType;

import java.util.EnumMap;
import java.util.Objects;

public class rpg_game extends Game {
	private static final String TAG = rpg_game.class.getSimpleName();
	private EnumMap<ScreenType, AbstractScreen> screenCache;
	private FitViewport screenViewport;
	private World world;
	private WorldContactListener worldContactListener;
	private Box2DDebugRenderer box2DDebugRenderer;
	private float accumulator;
	private static final float FIXED_TIME_STEP = 1 / 60f;

	public static final short BIT_PLAYER = 1<<0; // 0001
	//public static final short BIT_BOX = 1<<1; // 0010
	public static final short BIT_GROUND = 1<<2; // 0100

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		accumulator = 0;

		// Box2D initialization
		Box2D.init();
		world = new World(new Vector2(0, 0), true); // Initialized the gravity in our game
		worldContactListener = new WorldContactListener();
		world.setContactListener(worldContactListener);
		box2DDebugRenderer = new Box2DDebugRenderer();

		screenViewport = new FitViewport(9, 16); // I might need to change that to adapt it for a desktop game
		screenCache = new EnumMap<ScreenType, AbstractScreen>(ScreenType.class); // Initialized the screen cache
		setScreen(ScreenType.GAME); // Starting on the loading screen
	}

	public FitViewport getScreenViewport () { return screenViewport; }

	public World getWorld() {
		return world;
	}

	public Box2DDebugRenderer getBox2DDebugRenderer() {
		return box2DDebugRenderer;
	}

	// Switching between screens without overcharged the memory
	public void setScreen(final ScreenType screenType) {
		final Screen screen = screenCache.get(screenType); // gets what's in the screen cache
		if (screen == null) {
			// if screen is not created yet  -> create it
			try {
				Gdx.app.debug(TAG, "Creating new screen: " + screenType);
				final AbstractScreen newScreen = (AbstractScreen) ClassReflection.getConstructor(screenType.getScreenClass(), rpg_game.class).newInstance(this);
				screenCache.put(screenType, newScreen); // Put the newScreen in the cache so that it doesn't create another one next time
				setScreen (newScreen);
			} catch (ReflectionException e) {
				throw new GdxRuntimeException("Screen " + screenType + " could not be created", e);
			}
		} else {
			Gdx.app.debug(TAG, "Switching to new screen" + screenType);
			setScreen(screen);
		}
	}

	@Override
	public void render() {
		super.render();

		// Gdx.app.debug(TAG, "" + Gdx.graphics.getDeltaTime());

		// Always the same speed for objects
		accumulator += Math.min(0.25f, Gdx.graphics.getDeltaTime());
		while(accumulator >= FIXED_TIME_STEP) {
			world.step(FIXED_TIME_STEP, 6, 2);
			accumulator -=  FIXED_TIME_STEP;
		}

		// final float alpha = accumulator / FIXED_TIME_STEP;
	}

	@Override
	public void dispose() {
		super.dispose();
		box2DDebugRenderer.dispose();
		world.dispose();
	}
}
