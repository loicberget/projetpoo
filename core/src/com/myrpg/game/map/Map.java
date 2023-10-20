package com.myrpg.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.utils.Array;

public class Map {
    public static final String TAG = Map.class.getSimpleName();
    private final TiledMap tiledMap;
    private final Array<CollisionArea> collisionAreas;

    public Map(final TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        collisionAreas = new Array<>();

        parseCollisionLayer();
    }

    private void parseCollisionLayer() {
        // Get the collision layers that is defined in the map (in Tiled)
        final MapLayer collisionLayer = tiledMap.getLayers().get("collision");
        if(collisionLayer == null) {
            Gdx.app.debug(TAG, "No collision layer found!");
            return;
        }

        final MapObjects mapObjects = collisionLayer.getObjects();
        if (mapObjects == null) {
            Gdx.app.debug(TAG, "No collision objects found!");
            return;
        }

        // Loop through all the objects in the collision layer
        for(final MapObject mapObj : mapObjects) {
            // If there is a polygon object -> create a collision area from it
            if(mapObj instanceof PolygonMapObject) {
                final PolygonMapObject polygonMapObject = (PolygonMapObject) mapObj;
                final Polygon polygon = polygonMapObject.getPolygon();
                collisionAreas.add(new CollisionArea(polygon.getX(), polygon.getY(), polygon.getVertices()));
            } else {
                Gdx.app.debug(TAG, "Unknown map object found!");
            }
        }
    }

    // returns the collision areas
    public Array<CollisionArea> getCollisionAreas() {
        return collisionAreas;
    }
}