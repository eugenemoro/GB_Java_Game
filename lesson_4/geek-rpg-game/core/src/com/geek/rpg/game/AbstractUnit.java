package com.geek.rpg.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.geek.rpg.game.effects.DefenceStanceEffect;
import com.geek.rpg.game.effects.Effect;
import com.geek.rpg.game.effects.RegenerationEffect;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUnit {
    protected GameScreen game;
    protected Texture texture;
    protected Texture textureHpBar;
    protected String name;
    protected int hp;
    protected int maxHp;

    protected int level;

    protected Rectangle rect;

    // Primary Stats
    protected int strength;
    protected int dexterity;
    protected int endurance;
    protected int defence;
    protected int spellpower;

    protected Vector2 position;

    protected boolean flip;

    protected float attackAction;
    protected float takeDamageAction;

    protected ActiveEffectsSystem effects;

    public int getLevel() {
        return level;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getRect() {
        return rect;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public AbstractUnit(GameScreen game, Vector2 position, Texture texture) {
        this.game = game;
        this.position = position;
        this.texture = texture;
        this.rect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
        this.effects = new ActiveEffectsSystem();
        Pixmap pixmap = new Pixmap(90, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 1);
        pixmap.fill();
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fillRectangle(2, 2, 86, 16);
        this.textureHpBar = new Texture(pixmap);
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        this.rect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public void takeDamage(int dmg) {
        this.takeDamageAction = 1.0f;
        hp -= dmg;
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
        effects.effectsLifeTimeCheck();
    }

    public void heal(float percent) {
        int prevHp = hp;
        hp += maxHp * percent;
        if (hp > maxHp) {
            hp = maxHp;
        }
        game.getInfoSystem().addMessage("HP +" + (hp - prevHp), this, FlyingText.Colors.GREEN);
    }

    public void defenceStance(int rounds) {
        DefenceStanceEffect dse = new DefenceStanceEffect();
        dse.start(game.getInfoSystem(), this, rounds);
        effects.add(dse);
    }

    public void regenerate(int rounds) {
        RegenerationEffect re = new RegenerationEffect();
        re.start(game.getInfoSystem(), this, rounds);
        effects.add(re);
    }

    public void meleeAttack(AbstractUnit enemy) {
        attackAction = 1.0f;
        if (!Calculator.isTargetEvaded(this, enemy)) {
            int dmg = Calculator.getMeleeDamage(this, enemy);
            enemy.takeDamage(dmg);
            game.getInfoSystem().addMessage("-" + dmg, enemy, FlyingText.Colors.RED);
        } else {
            game.getInfoSystem().addMessage("MISS", enemy, FlyingText.Colors.WHITE);
        }
    }
}
