package PooQuest.utilities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import PooQuest.manager.ResourceManager;

import java.util.Arrays;

public class Utilities {
    public static Animation<TextureRegion> createAnimationFromTx(String texturePath, int row, int nb_cols, int framewidth, int frameheight) {
        ResourceManager resourceManager = ResourceManager.getInstance();
        resourceManager.loadTextureAsset(texturePath);
        TextureRegion[][] textureFrames = TextureRegion.split(resourceManager.getTextureAsset(texturePath), framewidth, frameheight);
        return new Animation<>(
                0.1f,
                new Array<>(Arrays.stream(textureFrames[row]).limit(nb_cols).toArray(TextureRegion[]::new)),
                Animation.PlayMode.LOOP);
    }
}
