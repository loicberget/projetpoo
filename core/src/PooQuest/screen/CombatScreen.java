package PooQuest.screen;

import PooQuest.PooQuest;
import PooQuest.character.PlayerCharacter;
import PooQuest.manager.ResourceManager;
import PooQuest.ui.CombatUI;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class CombatScreen extends AbstractScreen {

    public boolean isCombatOver() {
        return combatState != CombatState.COMBAT;
    }

    public void endCombat() {
        context.setScreen(ScreenType.GAME);
    }

    public enum CombatState {
        DRAW, WIN, LOSE, COMBAT
    }
    private PlayerCharacter player = PlayerCharacter.getInstance();
    private int foeMaxHealth;
    private int foeCurrentHealth;
    private int playerMaxHealth;
    private int playerCurrentHealth;
    private int playerMaxMana;
    private int playerCurrentMana;
    private String turnText = "";
    private int experience;
    private int money;
    private CombatState combatState = CombatState.COMBAT;
    Random rand = new Random();

    public CombatScreen(PooQuest context, ResourceManager resourceManager) {
        super(context, resourceManager);
    }

    @Override
    protected Table getScreenUI(Skin skin) {
        return new CombatUI(stage, resourceManager.skin, this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        ((CombatUI) screenUI).render(delta);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    public void resetCombatScreen() {
        turnText = "";
        combatState = CombatState.COMBAT;
        playerMaxHealth = player.getCharacterClass().getBaseHealth() + player.getCharacterClass().getBaseHealth() * player.getLevel() / 10;
        playerMaxMana = player.getCharacterClass().getBaseMana() + player.getCharacterClass().getBaseMana() * player.getLevel() / 10;
        playerCurrentHealth = playerMaxHealth;
        playerCurrentMana = playerMaxMana;

        foeMaxHealth = playerMaxHealth - 40;
        foeCurrentHealth = foeMaxHealth;
    }

    @Override
    public void show() {
        super.show();
        screenUI.setVisible(true);
        experience = 0;
        money = 0;
        resetCombatScreen();
    }

    @Override
    public void hide() {
        super.hide();
        screenUI.setVisible(false);
    }

    public int getPlayerMaxHealth() {
        return playerMaxHealth;
    }

    public int getPlayerCurrentHealth() {
        return playerCurrentHealth;
    }

    public int getPlayerMaxMana() {
        return playerMaxMana;
    }

    public int getPlayerCurrentMana() {
        return playerCurrentMana;
    }

    public int getFoeMaxHealth() {
        return foeMaxHealth;
    }

    public int getFoeCurrentHealth() {
        return foeCurrentHealth;
    }

    public void playTurn(CombatUI.attackType attackType) {
        int baseDamage = player.getEquipment().getWeaponValue();
        int baseFoeDamage = player.getLevel() * 2;
        int baseDefense = player.getEquipment().getArmorValue();
        int tmp = 0;
        switch (attackType) {
            case STRONG:
                if (rand.nextInt(100) < 50) {
                    tmp = baseDamage * 2;
                    foeCurrentHealth -= tmp;
                    turnText = "You hit the enemy for " + tmp + " !";
                } else {
                    turnText = "You missed!";
                }
                break;
            case LIGHT:
                if (rand.nextInt(100) < 80) {
                    tmp = baseDamage;
                    foeCurrentHealth -= tmp;
                    turnText = "You hit the enemy for " + tmp + "HP !";
                } else {
                    turnText = "You missed!";
                }
                break;
            case HEAL:
                if (playerCurrentMana >= 10) {
                    tmp = playerMaxHealth / 10;
                    playerCurrentHealth += tmp;
                    playerCurrentMana -= 10;
                    turnText = "You healed yourself for " + tmp + "HP !";
                } else {
                    turnText = "You don't have enough mana!";
                }
                break;
            case SPECIAL:
                if (playerCurrentMana >= 20) {
                    tmp = baseDamage * 3;
                    foeCurrentHealth -= tmp;
                    playerCurrentMana -= 20;
                    turnText = "You hit the enemy for " + tmp + "HP !";
                } else {
                    turnText = "You don't have enough mana!";
                }
                break;
        }

        if (rand.nextInt(100) < 90) {
            tmp = baseFoeDamage * (500 - baseDefense) / 500 + rand.nextInt(25);
            playerCurrentHealth -= tmp;
            turnText += "\nEnemy hit you for " + tmp + "HP !";
        } else {
            turnText += "\nEnemy missed!";
        }

        if (foeCurrentHealth <= 0 && playerCurrentHealth <= 0) {
            turnText += "\nYou killed the beast but suffer an injury!";
            player.endCombatDraw();
            combatState = CombatState.DRAW;
        } else if (foeCurrentHealth <= 0) {
            turnText += "\nYou won!";
            player.endCombatWin();
            combatState = CombatState.WIN;
        } else if (playerCurrentHealth <= 0) {
            player.endCombatLose();
            turnText += "\nYou died!";
            combatState = CombatState.LOSE;
        }
    }

    public String getTurnText() {
        return turnText;
    }
}
