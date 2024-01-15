package PooQuest.character;

public class Weapon extends Item {
    public Weapon(String name, String iconPath, int value) {
        super(name, iconPath, value);
    }

    @Override
    public void use() {
        PlayerCharacter player = PlayerCharacter.getInstance();
        if(player.equipment.getWeapon() == null) {
            player.equipment.setWeapon(this);
        } else {
            player.addItem(player.equipment.getWeapon());
            player.equipment.setWeapon(this);
        }
        player.removeItem(this);
    }
}
