package PooQuest.character;

public class Item {
    private String name;
    private String iconPath;
    private int value;

    public Item(String name, String iconPath, int value) {
        this.name = name;
        this.iconPath = iconPath;
        this.value = value;
    }

    public String getIconpath() {
        return iconPath;
    }

    public String getName() {
        return name;
    }


    public void use() {
    }

    public int getValue() {
        return value;
    }
}
