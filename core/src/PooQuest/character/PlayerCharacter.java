package PooQuest.character;

import PooQuest.PooQuest;
import PooQuest.entities.Entity;
import PooQuest.entities.EntitySprite;
import PooQuest.profile.ProfileManager;
import PooQuest.utilities.Direction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerCharacter extends Entity {
    private static PlayerCharacter instance;
    private static HashMap<Integer, Direction> directionKeyMap = new HashMap<>();

    static {
        directionKeyMap.put(Input.Keys.LEFT, Direction.LEFT);
        directionKeyMap.put(Input.Keys.RIGHT, Direction.RIGHT);
        directionKeyMap.put(Input.Keys.UP, Direction.UP);
        directionKeyMap.put(Input.Keys.DOWN, Direction.DOWN);
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    private CharacterClass characterClass;
    private int level;
    private int experience;
    private int money;
    private Array<Item> inventory = new Array<>();
    public Equipment equipment = new Equipment();

    public Array<Item> getInventory() {
        return inventory;
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int money) {
        this.money += money;
    }

    public void removeMoney(int money) {
        this.money -= money;
    }

    public static PlayerCharacter getInstance() {
        if (instance == null) {
            instance = new PlayerCharacter();
        }
        return instance;
    }

    private PlayerCharacter() {
        defineBodyAndFixtureForPlayer();
        String characterClassName = ProfileManager.getInstance().getProperty("playerClass", String.class);
        try {
            this.characterClass = (CharacterClass) CharacterClass.classMap.get(characterClassName).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (ProfileManager.getInstance().isNewProfile()) {
            level = 1;
            experience = 0;
            money = 30;
        } else {
            level = ProfileManager.getInstance().getProperty("playerLevel", Integer.class);
            experience = ProfileManager.getInstance().getProperty("playerExperience", Integer.class);
            money = ProfileManager.getInstance().getProperty("playerMoney", Integer.class);
            Array<String> savedEquipmentNames = new Array<String>(ProfileManager.getInstance().getProperty("playerEquipment", Array.class));
            for (String itemName : savedEquipmentNames) {
                equipment.equip(ItemHashMap.getInstance().elements.get(itemName));
            }
            Array<String> savedItemNames = new Array<String>(ProfileManager.getInstance().getProperty("playerInventory", Array.class));
            for (String itemName : savedItemNames) {
                inventory.add(ItemHashMap.getInstance().elements.get(itemName));
            }
        }
        sprite = new EntitySprite();
        sprite.setSpritePath(characterClass.spritePath);
    }

    private void defineBodyAndFixtureForPlayer() {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 0);

        fixtureDef = new FixtureDef();
        fixtureDef.density = 0;
        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = PooQuest.BIT_PLAYER;
        fixtureDef.filter.maskBits = PooQuest.BIT_GROUND;

        final PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(0.5f, 0.5f);
        fixtureDef.shape = boxShape;
    }

    public void processInput() {
        if (!isWalking()) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                setWalking(directionKeyMap.get(Input.Keys.LEFT));
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                setWalking(directionKeyMap.get(Input.Keys.RIGHT));
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                setWalking(directionKeyMap.get(Input.Keys.UP));
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                setWalking(directionKeyMap.get(Input.Keys.DOWN));
            }
        }
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.removeValue(item, true);
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getExperience() {
        return experience;
    }

    public void endCombatDraw() {
        if (level < 10) {
            experience += 10;
            money += 10;
        } else {
            experience += (int) (Math.log(level) * 10);
            money += (int) (Math.log(level) * 10);
        }

        calculateLevel();

        save();
    }

    public void endCombatLose() {
        if (level < 10) {
            experience += 5;
            money += 5;
        } else {
            experience += (int) (Math.log(level) * 5);
            money += (int) (Math.log(level) * 5);
        }

        calculateLevel();

        save();
    }

    public void endCombatWin() {
        if (level < 10) {
            experience += 20;
            money += 20;
        } else {
            experience += (int) (Math.log(level) * 20);
            money += (int) (Math.log(level) * 20);
        }

        calculateLevel();

        save();
    }

    private void calculateLevel() {
        if (level < 10 && experience >= 100) {
            level++;
            experience = 0;
        } else if (level >= 10 && experience >= (int) (Math.log(level) * 100)) {
            level++;
            experience = 0;
        }
    }

    public void save() {
        Array<String> itemNames = new Array<>();
        for(Item item : inventory) {
            itemNames.add(item.getName());
        }

        Array<String> equipmentNames = new Array<>();
        if(equipment.getWeapon() != null)
            equipmentNames.add(equipment.getWeapon().getName());
        if(equipment.getFeet() != null)
            equipmentNames.add(equipment.getFeet().getName());
        if(equipment.getChest() != null)
            equipmentNames.add(equipment.getChest().getName());
        if(equipment.getHands() != null)
            equipmentNames.add(equipment.getHands().getName());
        if(equipment.getHead() != null)
            equipmentNames.add(equipment.getHead().getName());
        if(equipment.getPotion() != null)
            equipmentNames.add(equipment.getPotion().getName());
        ProfileManager.getInstance().setProperty("playerEquipment", equipmentNames);
        ProfileManager.getInstance().setProperty("playerInventory", itemNames);
        ProfileManager.getInstance().setProperty("playerMoney", getMoney());
        ProfileManager.getInstance().setProperty("playerLevel", getLevel());
        ProfileManager.getInstance().setProperty("playerExperience", getExperience());
        ProfileManager.getInstance().saveProfile();
    }
}
