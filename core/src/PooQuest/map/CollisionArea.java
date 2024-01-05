package PooQuest.map;

import PooQuest.PooQuest;

public class CollisionArea {
    private final float x;
    private final float y;
    private final float[] vertices;

    public CollisionArea(final float x, final float y, final float[] vertices) {
        //this.startLocation = new Vector2(x * UNIT_SCALE, y * UNIT_SCALE);
        this.x = x * PooQuest.UNIT_SCALE;
        this.y = y * PooQuest.UNIT_SCALE;
        this.vertices = vertices;
        for(int i = 0; i < vertices.length; i+=2) {
            vertices[i] = vertices[i] * PooQuest.UNIT_SCALE;
            vertices[i+1] = vertices[i+1] * PooQuest.UNIT_SCALE;
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
