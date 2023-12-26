package com.myrpg.game.entities;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.myrpg.game.manager.ResourceManager;

public class Entity {
    private static final String TAG = Entity.class.getSimpleName();
    private static final String _defaultSpritePath = "sprites/characters/Warrior.png";
    private String _entityID;
    private Animation<TextureRegion> _walkLeftAnimation;
    private Animation<TextureRegion> _walkRightAnimation;
    private Animation<TextureRegion> _walkUpAnimation;
    private Animation<TextureRegion> _walkDownAnimation;
    private Array<TextureRegion> _walkLeftFrames;
    private Array<TextureRegion> _walkRightFrames;
    private Array<TextureRegion> _walkUpFrames;
    private Array<TextureRegion> _walkDownFrames;
    protected Vector2 _currentPlayerPosition;
    protected State _state = State.IDLE;
    protected float _frameTime = 0f;
    protected Sprite _frameSprite = null;
    protected TextureRegion _currentFrame = null;
    public final int FRAME_WIDTH = 16;
    public final int FRAME_HEIGHT = 16;
    public Entity.Direction lastDirection = null;
    public Body body;
    private float baseVelocity = 4f;
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

    public enum Direction {
        UP, RIGHT, DOWN, LEFT
    }

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
        _frameTime = (_frameTime + delta) % 5; //Want to avoid overflow
    }

    public void init(float startX, float startY) {
        this._currentPlayerPosition.x = startX;
        this._currentPlayerPosition.y = startY;
    }

    private void loadDefaultSprite() {
        Texture texture = ResourceManager.getTextureAsset(_defaultSpritePath);
        TextureRegion[][] textureFrames = TextureRegion.split(texture,
                FRAME_WIDTH, FRAME_HEIGHT);
        _frameSprite = new Sprite(textureFrames[0][0].getTexture(),
                0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        _currentFrame = textureFrames[0][0];
    }

    private void loadAllAnimations() {
        //Walking animation
        Texture texture = ResourceManager.getTextureAsset
                (_defaultSpritePath);
        TextureRegion[][] textureFrames = TextureRegion.split
                (texture, FRAME_WIDTH, FRAME_HEIGHT);
        _walkDownFrames = new Array<>(4);
        _walkLeftFrames = new Array<>(4);
        _walkRightFrames = new Array<>(4);
        _walkUpFrames = new Array<>(4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                TextureRegion region = textureFrames[i][j];
                if (region == null) {
                    Gdx.app.debug(TAG, "Got null animation frame " + i + "," + j);
                }
                switch (i) {
                    case 0:
                        _walkDownFrames.insert(j, region);
                        break;
                    case 1:
                        _walkLeftFrames.insert(j, region);
                        break;
                    case 2:
                        _walkRightFrames.insert(j, region);
                        break;
                    case 3:
                        _walkUpFrames.insert(j, region);
                        break;
                }
            }
        }
        _walkDownAnimation = new Animation<>(0.25f, _walkDownFrames,
                Animation.PlayMode.LOOP);
        _walkLeftAnimation = new Animation<>(0.25f, _walkLeftFrames,
                Animation.PlayMode.LOOP);
        _walkRightAnimation = new Animation<>(0.25f, _walkRightFrames,
                Animation.PlayMode.LOOP);
        _walkUpAnimation = new Animation<>(0.25f, _walkUpFrames,
                Animation.PlayMode.LOOP);
    }

    public void dispose() {
        ResourceManager.unloadAsset(_defaultSpritePath);
    }

    public Sprite getFrameSprite() {
        return _frameSprite;
    }

    public TextureRegion getFrame() {
        return _currentFrame;
    }

    public Vector2 getCurrentPosition() {
        return _currentPlayerPosition;
    }

    public void setCurrentPosition(float currentPositionX, float
            currentPositionY) {
        _frameSprite.setX(currentPositionX);
        _frameSprite.setY(currentPositionY);
        this._currentPlayerPosition.x = currentPositionX;
        this._currentPlayerPosition.y = currentPositionY;
    }

    public void setDirectionAnimation(Direction direction) {
        //Look into the appropriate variable when changing position
        switch (direction) {
            case DOWN:
                _currentFrame = _walkDownAnimation.getKeyFrame(_frameTime);
                break;
            case LEFT:
                _currentFrame = _walkLeftAnimation.getKeyFrame(_frameTime);
                break;
            case UP:
                _currentFrame = _walkUpAnimation.getKeyFrame(_frameTime);
                break;
            case RIGHT:
                _currentFrame = _walkRightAnimation.getKeyFrame(_frameTime);
                break;
            default:
                break;
        }
    }
}
