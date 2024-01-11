package PooQuest.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.TimeUtils;

public class GameUI extends Table {
    private final Window inventoryWindow;
    private long lastInventoryToggle;
    private int INV_TOGGLE_COOLDOWN = 250;
    private TextButton vendorPrompt;
    private Window vendorWindow;

    private final String vendorPromptText = "Press E to interact";

    public GameUI(final Stage stage, final Skin skin) {
        super(skin);
        Gdx.input.setInputProcessor(stage);
        this.bottom().padBottom(50);
        this.setFillParent(true);

        lastInventoryToggle = TimeUtils.millis();

        inventoryWindow = createInventoryWindow(skin);

        vendorWindow = createVendorWindow(skin);

        vendorPrompt = createVendorPrompt(skin);
    }

    private TextButton createVendorPrompt(Skin skin) {
        TextButton p = new TextButton(vendorPromptText, skin);
        p.setVisible(false);
        add(p).center().bottom();
        return p;
    }


    private Window createVendorWindow(Skin skin) {
        final Window v = new Window("Vendor", skin);
        v.setVisible(false);
        add(v).center().row();
        return v;
    }

    private Window createInventoryWindow(Skin skin) {
        final Window i;
        i = new Window("Inventory", skin);
        i.setVisible(false);
        i.setFillParent(true);
        Table playerItems = new Table(skin);
        ScrollPane scrollPane = new ScrollPane(playerItems, skin);
        i.add(scrollPane);
        add(i).row();
        return i;
    }

    public void hideVendorPrompt() {
        vendorPrompt.setVisible(false);
    }

    public void showVendorPrompt() {
        if (!vendorWindow.isVisible())
            vendorPrompt.setVisible(true);
    }

    public void showVendorWindow() {
        vendorWindow.setVisible(true);
    }

    public void hideVendorWindow() {
        vendorWindow.setVisible(false);
    }

    public void showInventoryWindow() {
        inventoryWindow.setVisible(true);
    }

    public void hideInventory() {
        inventoryWindow.setVisible(false);
    }

    public void processInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            if (vendorPrompt.isVisible()) {
                hideVendorPrompt();
                showVendorWindow();
                showInventoryWindow();
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            if (vendorWindow.isVisible()) {
                hideVendorWindow();
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.I) &&
                TimeUtils.timeSinceMillis(lastInventoryToggle) > INV_TOGGLE_COOLDOWN) {
            lastInventoryToggle = TimeUtils.millis();
            if (inventoryWindow.isVisible()) {
                hideInventory();
            } else {
                showInventoryWindow();
            }
        }
    }


}
