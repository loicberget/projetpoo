package PooQuest.character;

public class Equipment {
    private ArmorHead head;
    private ArmorChest chest;
    private ArmorFeet feet;
    private ArmorHands hands;
    private Weapon weapon;
    private Potion potion;
    public ArmorHead getHead() {
        return head;
    }
    public void setHead(ArmorHead head) {
        this.head = head;
    }

    public ArmorChest getChest() {
        return chest;
    }

    public void setChest(ArmorChest chest) {
        this.chest = chest;
    }

    public ArmorFeet getFeet() {
        return feet;
    }

    public void setFeet(ArmorFeet feet) {
        this.feet = feet;
    }

    public ArmorHands getHands() {
        return hands;
    }

    public void setHands(ArmorHands hands) {
        this.hands = hands;
    }
    public Weapon getWeapon() {
        return weapon;
    }
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
    public Potion getPotion() {
        return potion;
    }
    public void setPotion(Potion potion) {
        this.potion = potion;
    }

    public void equip(Item item) {
        if (item instanceof ArmorHead) {
            setHead((ArmorHead) item);
        } else if (item instanceof ArmorChest) {
            setChest((ArmorChest) item);
        } else if (item instanceof ArmorFeet) {
            setFeet((ArmorFeet) item);
        } else if (item instanceof ArmorHands) {
            setHands((ArmorHands) item);
        } else if (item instanceof Weapon) {
            setWeapon((Weapon) item);
        } else if (item instanceof Potion) {
            setPotion((Potion) item);
        }
    }

    public int getArmorValue() {
        int armorValue = 0;
        if (head != null) {
            armorValue += head.getValue();
        }
        if (chest != null) {
            armorValue += chest.getValue();
        }
        if (feet != null) {
            armorValue += feet.getValue();
        }
        if (hands != null) {
            armorValue += hands.getValue();
        }
        return armorValue;
    }

    public int getWeaponValue() {
        int weaponValue = 1;
        if (weapon != null) {
            weaponValue += weapon.getValue();
        }
        return weaponValue;
    }
}
