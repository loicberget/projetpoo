package PooQuest.character;

public abstract class CharacterClass {
    private int baseHealth;
    private int baseMana;
    protected CharacterClass(int baseHealth, int baseMana){
        this.baseHealth = baseHealth;
        this.baseMana = baseMana;
    }
    public int getBaseHealth() {
        return baseHealth;
    }
    public int getBaseMana() {
        return baseMana;
    }

}
