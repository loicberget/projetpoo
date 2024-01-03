package com.myrpg.game.utilities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;

public class Utilities {
    public static Animation<TextureRegion> createAnimationFromTxRegion(TextureRegion[][] textureFrames, int row, int nb_cols){
        return new Animation<>(0.1f,
                new Array<>(Arrays.stream(textureFrames[row])
                        .limit(nb_cols)
                        .toArray(TextureRegion[]::new)),
                Animation.PlayMode.LOOP);
    }
}
