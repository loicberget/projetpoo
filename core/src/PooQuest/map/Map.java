package PooQuest.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import PooQuest.PooQuest;
import jdk.internal.access.JavaIOFileDescriptorAccess;

public class Map {
    public static final String TAG = Map.class.getSimpleName();
    private final TiledMap tiledMap;
    private final Array<CollisionArea> collisionAreas;
    private final Vector2 startLocation;
    private final Vector2 blacksmithLocation;
    private final Vector2 spellVendorLocation;


    public Map(final TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        collisionAreas = new Array<>();
        startLocation = new Vector2();
        spellVendorLocation = new Vector2();
        blacksmithLocation = new Vector2();

        parseStartLayer();
        parseGameObjectsLayer();
        parseCollisionLayer();
    }

    private void parseGameObjectsLayer(){
        final MapLayer gameObjects = tiledMap.getLayers().get("gameObjects");
        if(gameObjects == null) {
            Gdx.app.debug(TAG, "No gameObjects layer found!");
            return;
        }

        final MapObjects mapObjects = gameObjects.getObjects();
        if (mapObjects == null) {
            Gdx.app.debug(TAG, "No map objects found!");
            return;
        }
        for(final MapObject mapObj : mapObjects) {

            if (mapObj.getName().equals("blacksmith")) {
                // Get the position of the point
                float x = mapObj.getProperties().get("x", Float.class) * PooQuest.UNIT_SCALE;
                float y = mapObj.getProperties().get("y", Float.class) * PooQuest.UNIT_SCALE;

                // Set the start location using the position of the point
                blacksmithLocation.set(x, y);
            }
            if (mapObj.getName().equals("spellVendor")) {
                // Get the position of the point
                float x = mapObj.getProperties().get("x", Float.class) * PooQuest.UNIT_SCALE;
                float y = mapObj.getProperties().get("y", Float.class) * PooQuest.UNIT_SCALE;

                // Set the start location using the position of the point
                spellVendorLocation.set(x, y);
            }
        }
    }

    private void parseStartLayer() {
        // Get the start layer that is defined in the map (in Tiled)
        final MapLayer startLayer = tiledMap.getLayers().get("playerStartLocation");
        if(startLayer == null) {
            Gdx.app.debug(TAG, "No start layer found!");
            return;
        }

        final MapObjects mapObjects = startLayer.getObjects();
        if (mapObjects == null) {
            Gdx.app.debug(TAG, "No start objects found!");
            return;
        }

        // Loop through all the objects layer
        for(final MapObject mapObj : mapObjects) {
            // If there is a point object -> create a start location from it
            if (mapObj.getName().equals("startPoint")) {
                // Get the position of the point
                float x = mapObj.getProperties().get("x", Float.class) * PooQuest.UNIT_SCALE;
                float y = mapObj.getProperties().get("y", Float.class) * PooQuest.UNIT_SCALE;

                // Set the start location using the position of the point
                startLocation.set(x, y);
            } else {
                Gdx.app.debug(TAG, "Unknown map object found!");
            }
        }
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

    // returns the start location
    public Vector2 getStartLocation() {
        return startLocation;
    }

    public Vector2 getBlacksmithLocation() {
        return blacksmithLocation;
    }

    public Vector2 getSpellVendorLocation() {
        return spellVendorLocation;
    }

}
