package PooQuest.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class GameUI extends Table {
    public GameUI(final Stage stage, final Skin skin) {
        super(skin);
        TextButton vendorPrompt = new TextButton("Press E to interact", skin);
        stage.addActor(vendorPrompt);
        stage.draw();
    }
}
