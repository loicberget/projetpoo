package com.myrpg.game.entities;

import java.util.UUID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.myrpg.game.manager.ResourceManager;

import static com.myrpg.game.utilities.Utilities.createAnimationFromTxRegion;

public class Entity {
    private static final String TAG = Entity.class.getSimpleName();
    private static final String _defaultSpritePath = "sprites/characters/warrior-fists.png";
    private String _entityID;
    private static final int WALK_NB_COLS = 9;
    private static final int WALK_UP_ROW = 8;
    private static final int WALK_RIGHT_ROW = 9;
    private static final int WALK_DOWN_ROW = 10;
    private static final int WALK_LEFT_ROW = 11;
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

    public static final int DIRECTION_UP = 0, DIRECTION_RIGHT = 1, DIRECTION_DOWN = 2, DIRECTION_LEFT = 3;

    public Entity() {
        initEntity();
    }

    public void initEntity() {
        this._entityID = UUID.randomUUID().toString();
        this._currentPlayerPosition = new Vector2();
        ResourceManager.loadTextureAsset(_defaultSpritePath);
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
        Texture texture = ResourceManager.getTextureAsset(_defaultSpritePath);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
        _currentFrame = textureFrames[DIRECTION_DOWN][0];
    }

    private void loadAllAnimations() {
        Texture texture = ResourceManager.getTextureAsset(_defaultSpritePath);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
        _walkAnimations = new Array<>();
        _walkAnimations.set(DIRECTION_UP, createAnimationFromTxRegion(textureFrames, WALK_UP_ROW, WALK_NB_COLS));
        _walkAnimations.set(DIRECTION_RIGHT, createAnimationFromTxRegion(textureFrames, WALK_RIGHT_ROW, WALK_NB_COLS));
        _walkAnimations.set(DIRECTION_DOWN, createAnimationFromTxRegion(textureFrames, WALK_DOWN_ROW, WALK_NB_COLS));
        _walkAnimations.set(DIRECTION_LEFT, createAnimationFromTxRegion(textureFrames, WALK_LEFT_ROW, WALK_NB_COLS));
    }

    public void dispose() {
        ResourceManager.unloadAsset(_defaultSpritePath);
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
