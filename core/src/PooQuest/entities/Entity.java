package PooQuest.entities;

import PooQuest.PooQuest;
import PooQuest.map.Map;
import PooQuest.utilities.Direction;

import static PooQuest.utilities.Direction.*;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.HashMap;

public abstract class Entity {
    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;
    private static final float baseVelocity = 4f;
    private static final HashMap<Direction, Vector2> directionVectorMap;
    private static final float TILE_SIZE = 1f;
    public static final float TILE_HALF = TILE_SIZE/2;

    // Definition du body et des fixtures ainsi que des vecteurs de direction pour toutes les entités
    static {
        directionVectorMap = new HashMap<>();
        directionVectorMap.put(UP, new Vector2(0f, baseVelocity));
        directionVectorMap.put(DOWN, new Vector2(0f, -baseVelocity));
        directionVectorMap.put(LEFT, new Vector2(-baseVelocity, 0f));
        directionVectorMap.put(RIGHT, new Vector2(baseVelocity, 0f));
    }

    protected void defineBodyAndFixtureForNPCs() {
        bodyDef = new BodyDef();

        bodyDef.position.set(0, 0);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;

        fixtureDef = new FixtureDef();
        fixtureDef.density = 0;
        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = PooQuest.BIT_GROUND;
        fixtureDef.filter.maskBits = PooQuest.BIT_PLAYER;

        final PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(TILE_HALF, TILE_HALF);
        fixtureDef.shape = boxShape;
    }

    public void dispose() {
        fixtureDef.shape.dispose();
    }

    public Body body;
    protected EntitySprite sprite;
    public Vector2 position;
    private Vector2 startMovingPosition;

    public void setWalking(Direction dir) {
        sprite.setDirection(dir);
        startMovingPosition.set(body.getPosition());
        body.setLinearVelocity(directionVectorMap.get(dir));
    }

    public boolean isWalking() {
        return !body.getLinearVelocity().isZero();
    }

    public void spawn(World world, Vector2 position) {
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setTransform(position, 0);
        this.position = new Vector2(position);
        startMovingPosition = new Vector2();
        sprite.load();
    }

    private Vector2 getTileMid(Vector2 position) {
        return new Vector2(MathUtils.floor(position.x) + TILE_HALF, MathUtils.floor(position.y) + TILE_HALF);
    }

    public boolean hasReachedNextTile() {
        return startMovingPosition.dst(body.getPosition()) >= TILE_SIZE;
    }

    public void update(float delta) {
        position.set(body.getPosition());
        if(hasReachedNextTile()) {
            stopWalking();
        } else sprite.update(delta);
    }

    public TextureRegion getSpriteFrame() {
        return sprite.getFrame();
    }

    public void stopWalking() {
        body.setLinearVelocity(Vector2.Zero);
        body.setTransform(getTileMid(body.getPosition()), 0);
    }
}
