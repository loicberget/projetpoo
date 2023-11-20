package com.myrpg.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.myrpg.game.map.CollisionArea;
import com.myrpg.game.map.Map;
import com.myrpg.game.rpg_game;
import com.myrpg.game.ui.GameUI;

import static com.myrpg.game.rpg_game.*;

public class GameScreen extends AbstractScreen {
    public static final String TAG = GameScreen.class.getSimpleName();
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;
    private final Body player;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final AssetManager assetManager;
    private final OrthographicCamera gameCamera;
    private final GLProfiler profiler;
    private final com.myrpg.game.map.Map map;


    // constructor
    public GameScreen(final rpg_game context) {
        super(context);

        //viewport = context.getScreenViewport();


        // Instantiation of the private final variable
        this.assetManager = context.getAssetManager();
        this.gameCamera = context.getGameCamera();

        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, context.getSpriteBatch());
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();

        profiler = new GLProfiler(Gdx.graphics);
        profiler.enable();

        // set the map
        final TiledMap tiledMap = assetManager.get("map/map.tmx", TiledMap.class);
        mapRenderer.setMap(tiledMap);
        map = new Map(tiledMap);

        spawnCollisionAreas();

        // create a player
        bodyDef.position.set(map.getStartLocation());
        Gdx.app.debug(TAG, "Player starting location: " + bodyDef.position);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        player = world.createBody(bodyDef);
        player.setUserData("PLAYER");

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_GROUND;
        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(0.4f, 0.4f);
        fixtureDef.shape = pShape;
        player.createFixture(fixtureDef);
        pShape.dispose();
    }

    @Override
    protected Table getScreenUI(Skin skin) {
        return new GameUI(stage, skin);
    }

    private void resetBodyandFixtureDefinition(){
        bodyDef.position.set(0, 0);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = false;

        fixtureDef.density = 0;
        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = 0x0001;
        fixtureDef.filter.maskBits = -1;
    }

    private void spawnCollisionAreas(){
        for(final CollisionArea collisionArea : map.getCollisionAreas()) {
            resetBodyandFixtureDefinition();

            // create a room
            bodyDef.position.set(collisionArea.getX(), collisionArea.getY());
            bodyDef.fixedRotation = true;
            final Body body = world.createBody(bodyDef);
            body.setUserData("GROUND");

            fixtureDef.filter.categoryBits = BIT_GROUND;
            fixtureDef.filter.maskBits = -1;

            final ChainShape chainShape = new ChainShape();
            chainShape.createChain(collisionArea.getVertices());
            fixtureDef.shape = chainShape;
            body.createFixture(fixtureDef);
            chainShape.dispose();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        final float speedX;
        final float speedY;

        // Left and right movement
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            speedX = -10;
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            speedX = 10;
        } else {
            speedX = 0;
        }

        // Up and down movement
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            speedY = -10;
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            speedY = 10;
        } else {
            speedY = 0;
        }

        player.applyLinearImpulse(
                (speedX - player.getLinearVelocity().x) * player.getMass(),
                (speedY - player.getLinearVelocity().y) * player.getMass(),
                player.getWorldCenter().x,
                player.getWorldCenter().y,
                true
        );

        viewport.apply(true);
        mapRenderer.setView(gameCamera); // TODO :  Comparer avec les valeurs au breakpoint avec le fichier original (a retelecharger sur github)
        mapRenderer.render();
        box2DDebugRenderer.render(world, viewport.getCamera().combined);
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
