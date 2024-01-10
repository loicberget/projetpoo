package PooQuest.character;

import PooQuest.PooQuest;
import PooQuest.entities.Entity;
import PooQuest.entities.EntitySprite;
import PooQuest.profile.ProfileManager;
import PooQuest.utilities.Direction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.util.HashMap;

public class PlayerCharacter extends Entity {
    private static PlayerCharacter instance;
    private static HashMap<Integer, Direction> directionKeyMap = new HashMap<>();
    static {
        directionKeyMap.put(Input.Keys.LEFT, Direction.LEFT);
        directionKeyMap.put(Input.Keys.RIGHT, Direction.RIGHT);
        directionKeyMap.put(Input.Keys.UP, Direction.UP);
        directionKeyMap.put(Input.Keys.DOWN, Direction.DOWN);
    }
    private CharacterClass characterClass;
    private PlayerCharacter() {
        defineBodyAndFixtureForPlayer();
        String characterClassName = ProfileManager.getInstance().getProperty("playerClass", String.class);
        try {
            this.characterClass = (CharacterClass) CharacterClass.classMap.get(characterClassName).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        sprite = new EntitySprite();
        sprite.setSpritePath(characterClass.spritePath);
    }

    private void defineBodyAndFixtureForPlayer() {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 0);

        fixtureDef = new FixtureDef();
        fixtureDef.density = 0;
        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = PooQuest.BIT_PLAYER;
        fixtureDef.filter.maskBits = PooQuest.BIT_GROUND;

        final PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(0.5f, 0.5f);
        fixtureDef.shape = boxShape;
    }

    public static PlayerCharacter getInstance() {
        if(instance == null){
            instance = new PlayerCharacter();
        }
        return instance;
    }

    public void processInput() {
        if(!isWalking()){
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                setWalking(directionKeyMap.get(Input.Keys.LEFT));
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                setWalking(directionKeyMap.get(Input.Keys.RIGHT));
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                setWalking(directionKeyMap.get(Input.Keys.UP));
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                setWalking(directionKeyMap.get(Input.Keys.DOWN));
            }
        }
    }

}
