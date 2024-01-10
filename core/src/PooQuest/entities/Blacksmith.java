package PooQuest.entities;

public class Blacksmith extends Entity {
public Blacksmith() {
        defineBodyAndFixtureForNPCs();
        sprite = new EntitySprite();
        sprite.setSpritePath("sprites/characters/kiara-the-blacksmith.png");
    }

}
