package PooQuest.entities;

import PooQuest.character.*;
import PooQuest.manager.ResourceManager;
import com.badlogic.gdx.utils.Array;

public class Blacksmith extends Vendor {

    public void loadInventory() {
        inventory = new Array<>();
        inventory.add(new ArmorChest("Leather Armor", "icons/A_Armour01.png", 15));
        inventory.add(new ArmorChest("Iron Armor", "icons/A_Armour02.png", 30));
        inventory.add(new ArmorChest("Steel Armor", "icons/A_Armour03.png", 100));
        inventory.add(new ArmorChest("Gold Plated Armor", "icons/A_Armour04.png", 150));
        inventory.add(new ArmorChest("Dragon Armor", "icons/A_Armour05.png", 200));
        inventory.add(new ArmorFeet("Leather Shoes", "icons/A_Shoes01.png", 15));
        inventory.add(new ArmorFeet("Goblin Pelt Shoes", "icons/A_Shoes02.png", 30));
        inventory.add(new ArmorFeet("Iron Shoes", "icons/A_Shoes03.png", 60));
        inventory.add(new ArmorFeet("Mermaid Pelt Shoes", "icons/A_Shoes04.png", 90));
        inventory.add(new ArmorFeet("Elven Shoes", "icons/A_Shoes05.png", 150));
        inventory.add(new ArmorFeet("Enchanted Shoes", "icons/A_Shoes06.png", 200));
        inventory.add(new ArmorFeet("Dragonpelt Shoes", "icons/A_Shoes07.png", 250));
        inventory.add(new Weapon("Iron Axe", "icons/W_Axe001.png", 15));
        inventory.add(new Weapon("Steel Axe", "icons/W_Axe002.png", 30));
        inventory.add(new Weapon("Double Axe", "icons/W_Axe003.png", 60));
        inventory.add(new Weapon("Curved Axe", "icons/W_Axe004.png", 90));
        inventory.add(new Potion("Health Potion", "icons/P_Red01.png", 100));
        for (Item item : inventory) {
            ResourceManager.getInstance().loadTextureAsset(item.getIconpath());
        }
    }

    public Array<Item> getInventory() {
        return inventory;
    }
    public Blacksmith() {
        sprite = new EntitySprite();
        sprite.setSpritePath("sprites/characters/kiara-the-blacksmith.png");
        loadInventory();
    }

}
