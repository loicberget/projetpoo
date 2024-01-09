package PooQuest.character;

public abstract class CharacterClass {
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
