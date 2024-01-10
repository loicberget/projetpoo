package PooQuest.screen;

import PooQuest.character.PlayerCharacter;
import PooQuest.entities.Blacksmith;
import PooQuest.entities.Entity;
import PooQuest.entities.SpellVendor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import PooQuest.manager.ResourceManager;
import PooQuest.map.CollisionArea;
import PooQuest.map.Map;
import PooQuest.PooQuest;
import PooQuest.ui.GameUI;

public class GameScreen extends AbstractScreen {
    public static final String TAG = GameScreen.class.getSimpleName();
    private static final ResourceManager resourceManager = null;
    private static final float CHAR_SPRITE_X_OFFSET = 0.7f;
    private static final float CHAR_SPRITE_Y_OFFSET = 0.5f;
    private static final float CHAR_SPRITE_WIDTH = 1.3f;
    private static final float CHAR_SPRITE_HEIGHT = 1.3f;
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;
    private PlayerCharacter player;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final AssetManager assetManager;
    private final OrthographicCamera gameCamera;
    private final GLProfiler profiler;
    private final Map map;
    private Blacksmith blacksmith;
    private SpellVendor spellVendor;

    // constructor
    public GameScreen(final PooQuest context) {
        super(context, resourceManager);
        this.assetManager = context.getAssetManager();
        this.gameCamera = context.getGameCamera();
        mapRenderer = new OrthogonalTiledMapRenderer(null, PooQuest.UNIT_SCALE, context.getSpriteBatch());
        fixtureDef = new FixtureDef();
        bodyDef = new BodyDef();

        profiler = new GLProfiler(Gdx.graphics);
        profiler.enable();

        // set the map
        final TiledMap tiledMap = assetManager.get("map/map.tmx", TiledMap.class);
        mapRenderer.setMap(tiledMap);
        map = new Map(tiledMap);
    }

    @Override
    protected Table getScreenUI(Skin skin) {
        return new GameUI(stage, skin);
    }

    private void resetBodyandFixtureDefinition() {
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

    private void spawnCollisionAreas() {
        for (final CollisionArea collisionArea : map.getCollisionAreas()) {
            resetBodyandFixtureDefinition();

            bodyDef.position.set(collisionArea.getX(), collisionArea.getY());
            bodyDef.fixedRotation = true;
            final Body body = world.createBody(bodyDef);
            body.setUserData("GROUND");

            fixtureDef.filter.categoryBits = PooQuest.BIT_GROUND;
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
        spawnCollisionAreas();
        player = PlayerCharacter.getInstance();
        player.spawn(world, map.getStartLocation());
        blacksmith = new Blacksmith();
        blacksmith.spawn(world, map.getBlacksmithLocation());
        spellVendor = new SpellVendor();
        spellVendor.spawn(world, map.getSpellVendorLocation());
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        player.update(delta);
        player.processInput();

        viewport.apply(true);
        mapRenderer.setView(gameCamera); // TODO :  Comparer avec les valeurs au breakpoint avec le fichier original (a retelecharger sur github)
        mapRenderer.render();
        mapRenderer.getBatch().begin();
        mapRenderer.getBatch().draw(
                player.getSpriteFrame(),
                player.position.x - CHAR_SPRITE_X_OFFSET,
                player.position.y - CHAR_SPRITE_Y_OFFSET,
                CHAR_SPRITE_WIDTH,
                CHAR_SPRITE_HEIGHT);
        mapRenderer.getBatch().draw(
                blacksmith.getSpriteFrame(),
                blacksmith.position.x - CHAR_SPRITE_X_OFFSET,
                blacksmith.position.y - CHAR_SPRITE_Y_OFFSET,
                CHAR_SPRITE_WIDTH,
                CHAR_SPRITE_HEIGHT);
        mapRenderer.getBatch().draw(
                spellVendor.getSpriteFrame(),
                spellVendor.position.x - CHAR_SPRITE_X_OFFSET,
                spellVendor.position.y - CHAR_SPRITE_Y_OFFSET,
                CHAR_SPRITE_WIDTH,
                CHAR_SPRITE_HEIGHT);
        mapRenderer.getBatch().end();

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
