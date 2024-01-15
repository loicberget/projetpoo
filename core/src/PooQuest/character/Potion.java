package PooQuest.character;

public class Potion extends Item{
    public Potion(String name, String iconPath, int value) {
        super(name, iconPath, value);
    }
    @Override
    public void use() {
        PlayerCharacter player = PlayerCharacter.getInstance();
        if (player.equipment.getPotion() != null) {
            player.addItem(player.equipment.getPotion());
        }
        player.equipment.setPotion(this);
        player.removeItem(this);
    }
}
