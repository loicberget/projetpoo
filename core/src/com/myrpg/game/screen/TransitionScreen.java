package com.myrpg.game.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.myrpg.game.audio.AudioObserver;
import com.myrpg.game.rpg_game;
import com.myrpg.game.screen.menu.MusicScreen;
import com.myrpg.game.screen.transition.effects.TransitionEffect;

import java.util.ArrayList;
import java.util.Arrays;

public class TransitionScreen extends AbstractScreen {

    private rpg_game context;
    private ScreenType current;
    private ScreenType next;

    int currentTransitionEffect;
    ArrayList<TransitionEffect> transitionEffects;

    TransitionScreen(rpg_game context, ScreenType current, ScreenType next, ArrayList<TransitionEffect> transitionEffects) {
        super((rpg_game) context, null);
        this.current = current;
        this.next = next;
        this.transitionEffects = transitionEffects;
        this.currentTransitionEffect = 0;
        this.context = context;
    }

    @Override
    public void render(float delta) {
        if (next.getScreenClass() != MusicScreen.class) {
            Arrays.stream(AudioObserver.AudioTypeEvent.values())
                    .filter(e -> e.equals(current.getMusicTheme()))
                    .findFirst()
                    .filter(a -> !a.equals(next.getMusicTheme()))
                    .ifPresent(a -> notify(AudioObserver.AudioCommand.MUSIC_STOP, a));
        }

        if (currentTransitionEffect >= transitionEffects.size()) {
                context.setScreen(next);
            return;
        }

        transitionEffects.get(currentTransitionEffect).update(delta);
        transitionEffects.get(currentTransitionEffect).render(current, next);

        if (transitionEffects.get(currentTransitionEffect).isFinished()) {
            currentTransitionEffect++;
        }

    }

    @Override
    public void show() {
        // Nothing
    }

    @Override
    protected Table getScreenUI(Skin skin) {
        return null;
    }

    @Override
    public void resize(int width, int height) {
        // Nothing
    }

    @Override
    public void pause() {
        // Nothing
    }

    @Override
    public void resume() {
        // Nothing
    }

    @Override
    public void hide() {
        // Nothing
    }

    @Override
    public void dispose() {
    }
}
