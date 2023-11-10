package com.myrpg.game.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LoadingUI extends Table {
    private final ProgressBar progressBar;
    private final TextButton txtButton;
    public LoadingUI(final Stage stage, final Skin skin) {
        super(skin);
        setFillParent(true);

        progressBar = new ProgressBar(0, 1, 0.01f, false, skin, "default");
        progressBar.setAnimateDuration(1);

        txtButton = new TextButton("Loading...", skin, "huge");
        txtButton.getLabel().setWrap(true);

        add(txtButton).expand().fill().bottom().row();
        add(progressBar).expand().fillX().bottom().pad (20, 25, 20, 25);
        bottom();
        //setDebug(true, true);
    }

    public void setProgress (final float progress){
        progressBar.setValue(progress);
    }

}
