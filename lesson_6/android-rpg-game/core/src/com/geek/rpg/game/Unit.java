package com.geek.rpg.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.geek.rpg.game.actions.BaseAction;
import com.geek.rpg.game.effects.DefenceStanceEffect;
import com.geek.rpg.game.effects.Effect;
import com.geek.rpg.game.effects.RegenerationEffect;

import java.util.ArrayList;
import java.util.List;

public class Unit {
    private Unit target;
    private BattleScreen battleScreen;
    private Texture texture;
    private Texture textureHpBar;
    private String name;
    private int hp;
    private int maxHp;

    private int level;

    private Rectangle rect;

    private Autopilot autopilot;

    public Autopilot getAutopilot() {
        return autopilot;
    }

    public void setActionPanel(Group actionPanel) {
        this.actionPanel = actionPanel;
    }

    public void setAutopilot(Autopilot autopilot) {
        this.autopilot = autopilot;
    }

    public boolean isAI() {
        return autopilot != null;
    }

    private Stats stats;
    private Group actionPanel;

    public Stats getStats() {
        return stats;
    }

    public Group getActionPanel() {
        return actionPanel;
    }

    private Vector2 position;

    private boolean flip;

    private float attackAction;
    private float takeDamageAction;

    public void setAttackAction(float attackAction) {
        this.attackAction = attackAction;
    }

    public Unit getTarget() {
        return target;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public BattleScreen getBattleScreen() {
        return battleScreen;
    }

    protected List<Effect> effects;

    private List<BaseAction> actions;

    public List<BaseAction> getActions() {
        return actions;
    }

    public int getLevel() {
        return level;
    }

    public void setActions(List<BaseAction> actions) {
        this.actions = actions;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setTarget(Unit target) {
        this.target = target;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public Texture getTexture() {
        return texture;
    }

    public Unit(Texture texture, Stats stats) {
        this.texture = texture;
        this.effects = new ArrayList<Effect>();
        this.position = new Vector2(0, 0);
        this.actions = new ArrayList<BaseAction>();
        this.stats = stats;
        Pixmap pixmap = new Pixmap(90, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 1);
        pixmap.fill();
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fillRectangle(2, 2, 86, 16);
        this.textureHpBar = new Texture(pixmap);
    }

    public void recalculateSecondaryStats() {
        this.maxHp = 5 * stats.getEndurance();
        this.hp = this.maxHp;
    }

    public void setToMap(BattleScreen battleScreen, int cellX, int cellY) {
        this.battleScreen = battleScreen;
        this.position.set(battleScreen.getStayPoints()[cellX][cellY]);
        this.rect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
        this.recalculateSecondaryStats();
    }

    public void evade() {
        battleScreen.getInfoSystem().addMessage("MISS", this, FlyingText.Colors.WHITE);
    }

    public void render(SpriteBatch batch) {
        if (takeDamageAction > 0) {
            batch.setColor(1f, 1f - takeDamageAction, 1f - takeDamageAction, 1f);
        }
        float dx = (50f * (float) Math.sin((1f - attackAction) * 3.14f));
        if (flip) dx *= -1;
        float ang = 0;
        if (!isAlive()) ang = 90;
        batch.draw(texture, position.x + dx, position.y, 0, 0, texture.getWidth(), texture.getHeight(), 1, 1, ang, 0, 0, texture.getWidth(), texture.getHeight(), flip, false);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public void renderInfo(SpriteBatch batch, BitmapFont font) {
        batch.setColor(0.5f, 0, 0, 1);
        batch.draw(textureHpBar, position.x, position.y + 130);
        batch.setColor(0, 1, 0, 1);
        batch.draw(textureHpBar, position.x, position.y + 130, 0, 0, (int) ((float) hp / (float) maxHp * textureHpBar.getWidth()), 20);
        batch.setColor(1, 1, 1, 1);
        font.draw(batch, String.valueOf(hp), position.x, position.y + 149, 90, 1, false);
    }

    public void update(float dt) {
        if (takeDamageAction > 0) {
            takeDamageAction -= dt;
        }
        if (attackAction > 0) {
            attackAction -= dt;
        }
    }

    public void getTurn() {
        for (int i = effects.size() - 1; i >= 0; i--) {
            effects.get(i).tick();
            if (effects.get(i).isEnded()) {
                effects.get(i).end();
                effects.remove(i);
            }
        }
    }

    public void changeHp(int value) {
        int prevHp = hp;
        hp += value;
        if (hp > maxHp) {
            hp = maxHp;
        }
        if (hp < 0) {
            hp = 0;
        }
        if (value > 0) {
            battleScreen.getInfoSystem().addMessage("HP +" + (hp - prevHp), this, FlyingText.Colors.GREEN);
        } else if (value < 0) {
            takeDamageAction = 1.0f;
            battleScreen.getInfoSystem().addMessage("HP " + -(prevHp - hp), this, FlyingText.Colors.RED);
        } else {
            battleScreen.getInfoSystem().addMessage("0", this, FlyingText.Colors.WHITE);
        }
    }

    public void addEffect(Effect effect) {
        effects.add(effect);
    }
}
