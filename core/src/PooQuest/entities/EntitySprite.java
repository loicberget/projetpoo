package PooQuest.entities;

import PooQuest.manager.ResourceManager;
import PooQuest.utilities.Utilities;
import PooQuest.utilities.Direction;
import static PooQuest.utilities.Direction.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;

public class EntitySprite {
    private String spritePath;
    private final int WALK_NB_COLS = 9, WALK_UP_ROW = 8, WALK_LEFT_ROW = 9, WALK_DOWN_ROW = 10, WALK_RIGHT_ROW = 11, FRAME_WIDTH = 64, FRAME_HEIGHT = 64;
    private float _frameTime = 0f;
    private TextureRegion _currentFrame = null;
    private HashMap<Direction, Animation<TextureRegion>> _walkAnimations;
    private Direction currentDirection = DOWN;

    public void setSpritePath(String spritePath) {
        this.spritePath = spritePath;
    }

    public void update(float delta) {
        _frameTime = (_frameTime + delta) % WALK_NB_COLS;
        _currentFrame = _walkAnimations.get(currentDirection).getKeyFrame(_frameTime);
    }

    public void load() {
        ResourceManager.loadTextureAsset(spritePath);
        _walkAnimations = new HashMap<>();
        _walkAnimations.put(UP, Utilities.createAnimationFromTx(spritePath, WALK_UP_ROW, WALK_NB_COLS, FRAME_WIDTH, FRAME_HEIGHT));
        _walkAnimations.put(DOWN, Utilities.createAnimationFromTx(spritePath, WALK_DOWN_ROW, WALK_NB_COLS, FRAME_WIDTH, FRAME_HEIGHT));
        _walkAnimations.put(LEFT, Utilities.createAnimationFromTx(spritePath, WALK_LEFT_ROW, WALK_NB_COLS, FRAME_WIDTH, FRAME_HEIGHT));
        _walkAnimations.put(RIGHT, Utilities.createAnimationFromTx(spritePath, WALK_RIGHT_ROW, WALK_NB_COLS, FRAME_WIDTH, FRAME_HEIGHT));
        _currentFrame = _walkAnimations.get(currentDirection).getKeyFrame(_frameTime);
    }

    public void dispose() {
        ResourceManager.unloadAsset(spritePath);
    }

    public TextureRegion getFrame() {
        return _currentFrame;
    }

    public void setDirection(Direction d) {
        this.currentDirection = d;
    }
}
