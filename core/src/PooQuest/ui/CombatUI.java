package PooQuest.ui;

import PooQuest.character.PlayerCharacter;
import PooQuest.manager.ResourceManager;
import PooQuest.screen.CombatScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.concurrent.atomic.AtomicInteger;


public class CombatUI extends Table {
    private PlayerCharacter player = PlayerCharacter.getInstance();
    private ResourceManager resourceManager = ResourceManager.getInstance();
    private TextureRegion currentFrame;
    private Image currentEnemyImage;
    private Animation<TextureRegion> dragonAnimation;
    private Label playerHealthLabel = new Label("HP : 100/100", getSkin());
    private Label playerManaLabel = new Label("MP : 100/100", getSkin());
    private Label enemyHealthLabel = new Label("HP : 100/100", getSkin());
    float stateTime = 0f;
    private TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable();
    private CombatScreen combatScreen;
    private Dialog combatDialog;

    public CombatUI(Stage stage, Skin skin, CombatScreen combatScreen) {
        super(skin);
        this.combatScreen = combatScreen;

        Gdx.input.setInputProcessor(stage);
        setFillParent(true);

        combatDialog = createCombatDialog();

        Table playerTable = new Table();
        add(playerTable);
        playerHealthLabel.setColor(0.5f, 1, 0.5f, 1);
        playerManaLabel.setColor(0, 0.5f, 1, 1);
        playerTable.add(playerHealthLabel).row();
        playerTable.add(playerManaLabel).row();
        playerTable.add(createAttackTable());

        Table enemyTable = new Table();
        add(enemyTable);
        enemyHealthLabel.setColor(1, 0.5f, 0, 1);
        enemyTable.add(enemyHealthLabel).row();

        resourceManager.loadTextureAsset("sprites/dragonsprite.png");
        TextureRegion[] dragonFrames = new TextureRegion[45];
        for (int i = 0; i < 45; i++) {
            dragonFrames[i] = new TextureRegion(resourceManager.getTextureAsset("sprites/dragonsprite.png"), 0, i * 300, 300, 300);
        }
        dragonAnimation = new Animation(0.25f, dragonFrames);
        dragonAnimation.setPlayMode(Animation.PlayMode.LOOP);
        currentFrame = dragonAnimation.getKeyFrame(0.5f, true);
        textureRegionDrawable.setRegion(currentFrame);
        currentEnemyImage = new Image();
        currentEnemyImage.setDrawable(textureRegionDrawable);
        enemyTable.add(currentEnemyImage).expand().fill();
    }

    public enum attackType {
        STRONG,
        LIGHT,
        HEAL,
        SPECIAL
    }

    private Table createAttackTable() {
        Button strongAttack = new TextButton("Strong Attack", getSkin());
        strongAttack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                combatScreen.playTurn(attackType.STRONG);
                displayCombatDialog();
            }
        });
        Button lightAttack = new TextButton("Light Attack", getSkin());
        lightAttack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                combatScreen.playTurn(attackType.LIGHT);
                displayCombatDialog();
            }
        });
        Button heal = new TextButton("Heal", getSkin());
        heal.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                combatScreen.playTurn(attackType.HEAL);
                displayCombatDialog();
            }
        });
        Button specialAttack = new TextButton("Special Attack", getSkin());
        specialAttack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                combatScreen.playTurn(attackType.SPECIAL);
                displayCombatDialog();
            }
        });
        Table attackTable = new Table();
        attackTable.add(strongAttack).pad(10);
        attackTable.add(lightAttack).pad(10).row();
        attackTable.add(heal).pad(10);
        attackTable.add(specialAttack).pad(10);

        return attackTable;
    }

    private Dialog createCombatDialog() {
        Dialog combatDialog = new Dialog("Combat", getSkin());
        combatDialog.setMovable(false);
        combatDialog.setModal(true);
        combatDialog.setResizable(false);
        combatDialog.setKeepWithinStage(true);
        combatDialog.getContentTable().align(Align.center);
        combatDialog.button("OK").addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                combatDialog.hide();
                if(combatScreen.isCombatOver()){
                    combatScreen.endCombat();
                }
            }
        });


        return combatDialog;
    }

    private void displayCombatDialog() {
        combatDialog.getContentTable().clear();
        combatDialog.getContentTable().add(new Label(combatScreen.getTurnText(), getSkin()));
        combatDialog.show(getStage());
    }

    public void render(float delta) {
        currentFrame = dragonAnimation.getKeyFrame(stateTime, true);
        textureRegionDrawable.setRegion(currentFrame);
        enemyHealthLabel.setText("HP : " + combatScreen.getFoeCurrentHealth() + "/" + combatScreen.getFoeMaxHealth());
        playerHealthLabel.setText("HP : " + combatScreen.getPlayerCurrentHealth() + "/" + combatScreen.getPlayerMaxHealth());
        playerManaLabel.setText("MP : " + combatScreen.getPlayerCurrentMana() + "/" + combatScreen.getPlayerMaxMana());
        stateTime += delta;
    }
}
