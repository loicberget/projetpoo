package PooQuest.ui;

import PooQuest.character.Equipment;
import PooQuest.character.Item;
import PooQuest.character.PlayerCharacter;
import PooQuest.entities.Blacksmith;
import PooQuest.manager.ResourceManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameUI extends Table {
    private int INV_TOGGLE_COOLDOWN = 250;
    private long lastInventoryToggle;
    private TextButton vendorPrompt;
    private final String vendorPromptText = "Press E to interact";
    private Table vendorTable;
    private Table playerTable;
    private Table armorTable;
    private PlayerCharacter player = PlayerCharacter.getInstance();
    private Array<Item> playerInventory = player.getInventory();
    private Equipment equipment = player.getEquipment();
    private Window vendorWindow;
    private Window inventoryWindow;
    private Label inventoryLabel = new Label("Money : "+0, getSkin());
    private Window armorWindow;
    private Blacksmith blacksmith;

    public GameUI(final Stage stage, final Skin skin) {
        super(skin);
        Gdx.input.setInputProcessor(stage);
        this.setFillParent(true);
        this.defaults().pad(5,5,5,5);

        lastInventoryToggle = TimeUtils.millis();

        vendorWindow = createVendorWindow();
        add(vendorWindow).minHeight(100).maxHeight(200);

        inventoryWindow = createInventoryWindow();
        add(inventoryWindow).minHeight(100).maxHeight(200);

        armorWindow = createEquipmentWindow();
        add(armorWindow).row();

        vendorPrompt = createVendorPrompt(skin);
        add(vendorPrompt).colspan(2);

        TextButton quitButton = createQuitButton(skin);
        add(quitButton).colspan(2).row();
    }

    public void show(){
        loadVendorInventory(blacksmith.getInventory());
        loadPlayerInventory();
        loadEquipment();
    }

    public void update(){
        processInput();

        if (blacksmith.isNear(player))
            showVendorPrompt();
        else {
            hideVendorPrompt();
            hideVendorWindow();
        }

        loadPlayerInventory();
        loadVendorInventory(blacksmith.getInventory());
        loadEquipment();
    }

    public void setVendor(Blacksmith blacksmith) {
        this.blacksmith = blacksmith;
    }

    private Window createVendorWindow() {
        vendorTable = new Table(getSkin());
        Window v = new Window("Vendor", getSkin());
        v.setMovable(false);
        v.setVisible(false);
        vendorTable.align(Align.topLeft);
        vendorTable.defaults().pad(2,2,2,0);
        ScrollPane scrollPaneVendor = new ScrollPane(vendorTable, getSkin());
        scrollPaneVendor.setFadeScrollBars(false);
        v.add(scrollPaneVendor).minWidth(300);
        return v;
    }
    private Window createInventoryWindow() {
        playerTable = new Table(getSkin());
        Window v = new Window("Inventory", getSkin());
        v.setMovable(false);
        v.setVisible(false);
        playerTable.align(Align.topLeft);
        playerTable.defaults().pad(2,2,2,0);
        ScrollPane scrollPanePlayer = new ScrollPane(playerTable, getSkin());
        scrollPanePlayer.setFadeScrollBars(false);
        v.add(scrollPanePlayer).minWidth(300);
        return v;
    }

    private Window createEquipmentWindow() {
        armorTable = new Table(getSkin());
        Window v = new Window("Equipment", getSkin());
        v.setMovable(false);
        v.setVisible(false);
        armorTable.align(Align.topLeft);
        armorTable.defaults().pad(0,0,1,0);
        v.add(armorTable);
        return v;
    }

    public void loadVendorInventory(Array<Item> vendorInventory) {
        vendorTable.clear();
        for(Item item : vendorInventory) {
            vendorTable.add(new Image(ResourceManager.getInstance().getTextureAsset(item.getIconpath())));
            vendorTable.add(item.getName());
            Button buyButton = new TextButton("Buy", getSkin());
            buyButton.addListener((event) -> {
                if (event.toString().equals("touchDown") && player.getMoney() >= item.getValue()){
                    player.removeMoney(item.getValue());
                    playerInventory.add(item);
                }
                return true;
            });
            vendorTable.add(buyButton);
            vendorTable.add(Integer.toString(item.getValue()));
            vendorTable.row();
        }
    }

    public void loadPlayerInventory() {
        playerTable.clear();
        inventoryLabel.setText("Money: " + player.getMoney());
        playerTable.add(inventoryLabel).row();
        for(Item item : playerInventory) {
            playerTable.add(new Image(ResourceManager.getInstance().getTextureAsset(item.getIconpath())));
            playerTable.add(item.getName());

            if(blacksmith.isNear(player)){
                Button buyButton = new TextButton("Sell", getSkin());
                buyButton.addListener((event) -> {
                    if (event.toString().equals("touchDown")) {
                        playerInventory.removeValue(item, true);
                        player.addMoney(item.getValue());
                    }
                    return true;
                });
                playerTable.add(buyButton);
            }
            else {
                Button useButton = new TextButton("Equip", getSkin());
                useButton.addListener((event) -> {
                    if (event.toString().equals("touchDown")) {
                        item.use();
                    }
                    return true;
                });
                playerTable.add(useButton);
            }
            playerTable.add(Integer.toString(item.getValue()));
            playerTable.row();
        }
    }

    private void loadEquipment() {
        armorTable.clear();
        if(equipment.getChest() != null)
        {
            armorTable.add(new Image(ResourceManager.getInstance().getTextureAsset(equipment.getChest().getIconpath()))).row();
        }
        if(equipment.getHands() != null)
        {
            armorTable.add(new Image(ResourceManager.getInstance().getTextureAsset(equipment.getHands().getIconpath()))).row();
        }
        if(equipment.getFeet() != null)
        {
            armorTable.add(new Image(ResourceManager.getInstance().getTextureAsset(equipment.getFeet().getIconpath()))).row();
        }
        if(equipment.getHead() != null)
        {
            armorTable.add(new Image(ResourceManager.getInstance().getTextureAsset(equipment.getHead().getIconpath()))).row();
        }
        if(equipment.getPotion() != null)
        {
            armorTable.add(new Image(ResourceManager.getInstance().getTextureAsset(equipment.getPotion().getIconpath()))).row();
        }
        if(equipment.getWeapon() != null)
        {
            armorTable.add(new Image(ResourceManager.getInstance().getTextureAsset(equipment.getWeapon().getIconpath()))).row();
        }
    }
    private TextButton createVendorPrompt(Skin skin) {
        TextButton p = new TextButton(vendorPromptText, skin);
        p.setVisible(false);
        return p;
    }

    private TextButton createQuitButton(Skin skin) {
        TextButton p = new TextButton("Quit", skin);
        p.addListener((event) -> {
            if (event.toString().equals("touchDown")) {
                player.save();
                ResourceManager.getInstance().unloadALl();
                Gdx.app.exit();
            }
            return true;
        });
        return p;
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
        armorWindow.setVisible(true);
    }

    public void hideInventory() {
        inventoryWindow.setVisible(false);
        armorWindow.setVisible(false);
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
                hideInventory();
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
