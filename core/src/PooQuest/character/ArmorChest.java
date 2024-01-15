package PooQuest.character;

public class ArmorChest extends Item{
    public ArmorChest(String name, String iconPath, int value) {
        super(name, iconPath, value);
    }

    @Override
    public void use() {
        PlayerCharacter player = PlayerCharacter.getInstance();
        if(player.equipment.getChest() == null) {
            player.equipment.setChest(this);
        } else {
            player.addItem(player.equipment.getChest());
            player.equipment.setChest(this);
        }
        player.removeItem(this);
    }
}
