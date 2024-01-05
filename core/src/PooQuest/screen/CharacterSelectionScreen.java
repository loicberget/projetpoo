package PooQuest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import PooQuest.manager.ResourceManager;
import PooQuest.PooQuest;

// Logger imports


/**
 * This class represents the character selection screen in the game.
 */
public class CharacterSelectionScreen extends AbstractScreen {
    private Stage stageForCharacterSelection = new Stage(); // Stage for character selection
    private Table tableForCharacterSelection; // Table for character layout

    /**
     * Constructor for CharacterSelectionScreen.
     *
     * @param context              The main game class
     * @param resourceManager   Manager for game resources
     */
    public CharacterSelectionScreen(PooQuest context, ResourceManager resourceManager) {
        super(context, resourceManager);
        tableForCharacterSelection = createTable(); ; // Create the character selection table
    }

    @Override
    protected Table getScreenUI(Skin skin) {
        return null;
    }

    /**
     * Sets up the screen when it is shown.
     */
    @Override
    public void show() {
        stageForCharacterSelection.addActor(tableForCharacterSelection); // Add the table to the stage
        Gdx.input.setInputProcessor(stageForCharacterSelection); // Set input processor to the stage
    }

    /**
     * Renders the screen.
     *
     * @param delta Time since last frame
     */
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begin and end batch for drawing
        context.getSpriteBatch().begin();
        context.getSpriteBatch().end();

        // Draw the stage
        stageForCharacterSelection.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    /**
     * Disposes of screen resources when not needed anymore.
     */
    @Override
    public void dispose() {
        super.dispose();
        tableForCharacterSelection.remove(); // Remove the table
    }
}
