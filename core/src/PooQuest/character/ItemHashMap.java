package PooQuest.character;

import PooQuest.manager.ResourceManager;

import java.util.HashMap;

public class ItemHashMap {

    static ItemHashMap instance;
    public static final HashMap<String, Item> elements = new HashMap<>();
    public ItemHashMap(){
        elements.put("Leather Armor", new ArmorChest("Leather Armor", "icons/A_Armour01.png", 15));
        elements.put("Iron Armor", new ArmorChest("iron Armor", "icons/A_Armour02.png", 30));
        elements.put("Steel Armor", new ArmorChest("Steel Armor", "icons/A_Armour03.png", 100));
        elements.put("Gold Plated Armor", new ArmorChest("Gold Plated Armor", "icons/A_Armour04.png", 150));
        elements.put("Dragon Armor", new ArmorChest("Dragon Armor", "icons/A_Armour05.png", 200));
        elements.put("Leather Shoes", new ArmorFeet("Leather Shoes", "icons/A_Shoes01.png", 15));
        elements.put("Goblin Pelt Shoes", new ArmorFeet("Goblin Pelt Shoes", "icons/A_Shoes02.png", 30));
        elements.put("Iron Shoes", new ArmorFeet("Iron Shoes", "icons/A_Shoes03.png", 60));
        elements.put("Mermaid Pelt Shoes", new ArmorFeet("Mermaid Pelt Shoes", "icons/A_Shoes04.png", 90));
        elements.put("Elven Shoes", new ArmorFeet("Elven Shoes", "icons/A_Shoes05.png", 150));
        elements.put("Enchanted Shoes", new ArmorFeet("Enchanted Shoes", "icons/A_Shoes06.png", 200));
        elements.put("Dragonpelt Shoes", new ArmorFeet("Dragonpelt Shoes", "icons/A_Shoes07.png", 250));
        elements.put("Iron Axe", new Weapon("Iron Axe", "icons/W_Axe001.png", 15));
        elements.put("Steel Axe", new Weapon("Steel Axe", "icons/W_Axe002.png", 30));
        elements.put("Double Axe", new Weapon("Double Axe", "icons/W_Axe003.png", 60));
        elements.put("Curved Axe", new Weapon("Curved Axe", "icons/W_Axe004.png", 90));
        elements.put("Health Potion", new Potion("Health Potion", "icons/P_Red01.png", 100));
        for(Item item : elements.values()){
            ResourceManager.getInstance().loadTextureAsset(item.getIconpath());
        }
    }

    public static ItemHashMap getInstance() {
        if(instance == null){
            instance = new ItemHashMap();
        }
        return instance;
    }
}
