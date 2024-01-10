package PooQuest.entities;

import PooQuest.PooQuest;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Vendor extends Entity {
    public Vendor() {
        defineBodyAndFixtureForNPCs();
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

    public boolean isNear(Entity entity) {
        return position.dst(entity.position) < 1.5f;
    }
}
