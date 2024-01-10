package PooQuest.entities;

public class SpellVendor extends Entity {
    public SpellVendor() {
        defineBodyAndFixtureForNPCs();
        sprite = new EntitySprite();
        sprite.setSpritePath("sprites/characters/kiara-the-blacksmith.png");
    }
}
