package PooQuest.character;

public class ArmorHands extends Item {
    public ArmorHands(String name, String iconPath, int value) {
        super(name, iconPath, value);
    }

    @Override
    public void use() {
        PlayerCharacter player = PlayerCharacter.getInstance();
        if (player.equipment.getHands() == null) {
            player.equipment.setHands(this);

        } else {
            player.addItem(player.equipment.getHands());
            player.equipment.setHands(this);
        }
        player.removeItem(this);
    }
}
