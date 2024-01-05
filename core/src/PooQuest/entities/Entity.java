package PooQuest.entities;

import java.util.UUID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import PooQuest.manager.ResourceManager;
import PooQuest.utilities.Utilities;

public class Entity {
    private static final String TAG = Entity.class.getSimpleName();
    private static final String _entitySpritePath = "sprites/characters/warrior-fists.png";
    private String _entityID;
    private static final int WALK_NB_COLS = 9;
    private static final int WALK_UP_ROW = 8;
    private static final int WALK_LEFT_ROW = 9;
    private static final int WALK_DOWN_ROW = 10;
    private static final int WALK_RIGHT_ROW = 11;
    protected Vector2 _currentPlayerPosition;
    protected State _state = State.IDLE;
    protected float _frameTime = 0f;
    protected TextureRegion _currentFrame = null;
    public static final int FRAME_WIDTH = 64;
    public static final int FRAME_HEIGHT = 64;
    private int currentDirection = DIRECTION_DOWN;
    public Body body;
    private final float baseVelocity = 4f;
    private Array<Animation<TextureRegion>> _walkAnimations;

    public State getState() {
        return _state;
    }

    public float getBaseVelocity() {
        return baseVelocity;
    }

    public void setState(State state) {
        this._state = state;
    }

    public enum State {
        IDLE, WALKING
    }

    public static final int DIRECTION_UP = 0, DIRECTION_LEFT = 1, DIRECTION_DOWN = 2, DIRECTION_RIGHT = 3;

    public Entity() {
        this._entityID = UUID.randomUUID().toString();
        this._currentPlayerPosition = new Vector2();
        ResourceManager.loadTextureAsset(_entitySpritePath);
        loadDefaultSprite();
        loadAllAnimations();
    }

    public void update(float delta) {
        continueAnimation();
        _frameTime = (_frameTime + delta) % WALK_NB_COLS; //Want to avoid overflow
    }

    public void init(float startX, float startY) {
        this._currentPlayerPosition.x = startX;
        this._currentPlayerPosition.y = startY;
    }

    private void loadDefaultSprite() {
        Texture texture = ResourceManager.getTextureAsset(_entitySpritePath);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
        _currentFrame = textureFrames[DIRECTION_DOWN][0];
    }

    private void loadAllAnimations() {
        _walkAnimations = new Array<>();
        _walkAnimations.insert(DIRECTION_UP, Utilities.createAnimationFromTx(_entitySpritePath, WALK_UP_ROW, WALK_NB_COLS, FRAME_WIDTH, FRAME_HEIGHT));
        _walkAnimations.insert(DIRECTION_LEFT, Utilities.createAnimationFromTx(_entitySpritePath, WALK_LEFT_ROW, WALK_NB_COLS, FRAME_WIDTH, FRAME_HEIGHT));
        _walkAnimations.insert(DIRECTION_DOWN, Utilities.createAnimationFromTx(_entitySpritePath, WALK_DOWN_ROW, WALK_NB_COLS, FRAME_WIDTH, FRAME_HEIGHT));
        _walkAnimations.insert(DIRECTION_RIGHT, Utilities.createAnimationFromTx(_entitySpritePath, WALK_RIGHT_ROW, WALK_NB_COLS, FRAME_WIDTH, FRAME_HEIGHT));
    }

    public void dispose() {
        ResourceManager.unloadAsset(_entitySpritePath);
    }

    public TextureRegion getFrame() {
        return _currentFrame;
    }

    public Vector2 getCurrentPosition() {
        return _currentPlayerPosition;
    }

    public void setCurrentPosition(
            float currentPositionX,
            float currentPositionY) {
        this._currentPlayerPosition.x = currentPositionX;
        this._currentPlayerPosition.y = currentPositionY;
    }

    public void continueAnimation() {
        if(_state == State.WALKING)
            _currentFrame = _walkAnimations.get(currentDirection).getKeyFrame(_frameTime);
    }

    public int setCurrentDirection() {
        return currentDirection;
    }

    public void setDirection(int direction) {
        this.currentDirection = direction;
    }
}
