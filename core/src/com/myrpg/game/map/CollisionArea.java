package com.myrpg.game.map;

import com.badlogic.gdx.math.Vector2;

import static com.myrpg.game.rpg_game.UNIT_SCALE;

public class CollisionArea {
    private final float x;
    private final float y;
    private final float[] vertices;

    public CollisionArea(final float x, final float y, final float[] vertices) {
        //this.startLocation = new Vector2(x * UNIT_SCALE, y * UNIT_SCALE);
        this.x = x * UNIT_SCALE;
        this.y = y * UNIT_SCALE;
        this.vertices = vertices;
        for(int i = 0; i < vertices.length; i+=2) {
            vertices[i] = vertices[i] * UNIT_SCALE;
            vertices[i+1] = vertices[i+1] * UNIT_SCALE;
        }
    }

    public float[] getVertices() {
        return vertices;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
