package com.geek.rpg.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractUnit {
    protected RpgGame game;
    protected Texture texture;
    protected Texture textureHpBar;
    protected String name;
    protected int hp;
    protected int maxHp;
    private boolean dodged;

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

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getRect() {
        return rect;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public AbstractUnit(RpgGame game, Vector2 position, Texture texture) {
        this.game = game;
        this.position = position;
        this.texture = texture;
        this.rect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());

        Pixmap pixmap = new Pixmap(90, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();

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

    public void renderInfo(SpriteBatch batch) {
        batch.setColor(0.5f, 0, 0, 1);
        batch.draw(textureHpBar, position.x, position.y + 130);
        batch.setColor(0, 1, 0, 1);
        batch.draw(textureHpBar, position.x, position.y + 130, 0, 0, (int) ((float) hp / (float) maxHp * textureHpBar.getWidth()), 20);
        batch.setColor(1, 1, 1, 1);
    }

    public void update(float dt) {
        if (takeDamageAction > 0) {
            takeDamageAction -= dt;
        }
        if (attackAction > 0) {
            attackAction -= dt;
        }
    }

    public abstract void getTurn();

    public void meleeAttack(AbstractUnit enemy) {
        if (dodged) {
            this.defence -= 5;
            dodged = false;
        }
        int dmg = this.strength - enemy.defence;
        dmg = (int)(dmg * 0.8f + (float)dmg * Math.random() * 0.4f);
        if (dmg < 0) {
            dmg = 0;
        }
        int attackChance = 70 + (this.dexterity - enemy.dexterity) * 2 + (this.level - enemy.level) * 5;
        if (attackChance > 95) attackChance = 95;
        if (attackChance < 20) attackChance = 20;
        this.attackAction = 1.0f;
        if (Math.random() * 100 <= attackChance) {
            enemy.takeDamage(dmg);
            game.addMessage("-" + dmg, enemy.getPosition().x + 45, enemy.getPosition().y + 75);
        } else {
            game.addMessage("MISS", enemy.getPosition().x + 45, enemy.getPosition().y + 75);
        }
    }

    public void dodge(){
        if (dodged) {
            this.defence -= 5;
            dodged = false;
        }
        this.defence += 5;
        dodged = true;
        game.addMessage("Dodge", this.getPosition().x + 45, this.getPosition().y + 75);
    }

    public void rest(){
        if (dodged) {
            this.defence -= 5;
            dodged = false;
        }
        this.hp *= 1.15f;
        if (this.hp > maxHp) this.hp = maxHp;
        game.addMessage("Rest", this.getPosition().x + 45, this.getPosition().y + 75);
    }
}
