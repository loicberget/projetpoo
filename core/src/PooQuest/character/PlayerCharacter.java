package PooQuest.character;

import PooQuest.entities.Entity;
import PooQuest.entities.EntitySprite;
import PooQuest.utilities.Direction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

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
    private PlayerCharacter(CharacterClass characterClass){
        this.characterClass = characterClass;
        sprite = new EntitySprite();
        sprite.setSpritePath(characterClass.spritePath);
    }

    public static PlayerCharacter getInstance(CharacterClass characterClass){
        if(instance == null){
            instance = new PlayerCharacter(characterClass);
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
