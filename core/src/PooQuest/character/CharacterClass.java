package PooQuest.character;

import java.util.HashMap;

public abstract class CharacterClass {
    public static final HashMap<String, Class<?>> classMap = new HashMap<>();
    static {
        classMap.put("Warrior", Warrior.class);
        classMap.put("Mage", Mage.class);
        classMap.put("Thief", Thief.class);
    }
    protected String spritePath;
    protected int baseHealth;
    protected int baseMana;

    public void setSpritePath(String spritePath) {
        this.spritePath = spritePath;
    }
    public int getBaseHealth() {
        return baseHealth;
    }
    public int getBaseMana() {
        return baseMana;
    }

}
