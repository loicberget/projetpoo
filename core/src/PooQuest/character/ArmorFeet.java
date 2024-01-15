package PooQuest.character;

public class ArmorFeet extends Item {
    public ArmorFeet(String name, String iconPath, int value) {
        super(name, iconPath, value);
    }

    @Override
    public void use() {
        PlayerCharacter player = PlayerCharacter.getInstance();
        if(player.equipment.getFeet() == null) {
            player.equipment.setFeet(this);
        } else {
            player.addItem(player.equipment.getFeet());
            player.equipment.setFeet(this);
        }
        player.removeItem(this);
    }
}
