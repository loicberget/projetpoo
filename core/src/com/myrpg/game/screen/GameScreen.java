package com.myrpg.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.myrpg.game.rpg_game;

import java.awt.*;

import static com.myrpg.game.rpg_game.*;

public class GameScreen extends AbstractScreen {
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;
    private final Body player;

    // constructor
    public GameScreen(final rpg_game context) {
        super(context);

        // Instantiation of body and fixture definition
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();

        // create a player
        bodyDef.position.set(4.5f, 3);
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
        pShape.setAsBox(0.5f, 0.5f);
        fixtureDef.shape = pShape;
        player.createFixture(fixtureDef);
        pShape.dispose();

        // create a room
        bodyDef.position.set(0, 0);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        final Body body = world.createBody(bodyDef);
        body.setUserData("GROUND");

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_GROUND;
        fixtureDef.filter.maskBits = -1;
        final ChainShape chainShape = new ChainShape();
        chainShape.createLoop(new float []{1, 1, 1, 15, 8, 15, 8, 1});
        fixtureDef.shape = chainShape;
        body.createFixture(fixtureDef);
        chainShape.dispose();
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
            speedX = -3;
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            speedX = 3;
        } else {
            speedX = 0;
        }

        // Up and down movement
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            speedY = -3;
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            speedY = 3;
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

        // if any key is pressed -> change the screen
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            context.setScreen(ScreenType.LOADING);
        }

        viewport.apply(true);
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

    }
}
