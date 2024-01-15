package PooQuest.character;

public class ArmorHead extends Item {
    public ArmorHead(String name, String iconPath, int value) {
        super(name, iconPath, value);
    }

    @Override
    public void use() {
        PlayerCharacter player = PlayerCharacter.getInstance();
        if(player.equipment.getHead() == null) {
            player.equipment.setHead(this);
        } else {
            player.addItem(player.equipment.getHead());
            player.equipment.setHead(this);
        }
        player.removeItem(this);
    }
}
