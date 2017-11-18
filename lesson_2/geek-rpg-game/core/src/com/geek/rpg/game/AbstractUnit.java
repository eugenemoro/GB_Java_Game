package com.geek.rpg.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import sun.security.provider.SHA;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractUnit {

    protected Texture texture;
    protected String name;
    protected int hp;
    protected int maxHp;
    protected ShapeRenderer shapeRenderer = new ShapeRenderer();
    protected float HP_HEIGHT = 5.0f;
    protected Label.LabelStyle labelStyleWhite = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
    protected Label dmgLabel = new Label("", labelStyleWhite);
    protected String dmgText = "";

    protected int level;

    protected Rectangle rect;

    // Primary Stats
    protected int strength;
    protected int dexterity;
    protected int endurance;
    protected int spellpower;

    // Secondary Stats
    protected int defence;

    protected Vector2 position;

    protected boolean flip;

    protected float attackAction;
    protected float takeDamageAction;

    public Rectangle getRect() {
        return rect;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        this.rect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public void takeDamage(int dmg) {
        this.takeDamageAction = 1.0f;
        if ((float) Math.random() >= dexterity * 1.0f / 100) {
            hp -= dmg;
            if (hp <= 0) hp = 0;
            dmgText = "- " + dmg;
        } else {
            dmgText = "Dodge!";
        }
    }

    public void render() {
        Batch batch = new SpriteBatch();
        BitmapFont font = new BitmapFont();
        dmgLabel.setSize(texture.getWidth(), HP_HEIGHT);
        dmgLabel.setAlignment(Align.center);
        batch.begin();
        if (takeDamageAction > 0) {
            batch.setColor(1f, 1f - takeDamageAction, 1f - takeDamageAction, 1f);
        }
        float dx = (50f * (float) Math.sin((1f - attackAction) * 3.14f));
        if (flip) dx *= -1;
        if (hp > 0) {

            batch.draw(texture, position.x + dx, position.y, 0, 0, texture.getWidth(), texture.getHeight(), 1, 1, 0, 0, 0, texture.getWidth(), texture.getHeight(), flip, false);
            dmgLabel.setPosition((int) position.x + dx, position.y + texture.getHeight() + 3 *HP_HEIGHT);
            dmgLabel.setText(dmgText);
            dmgLabel.draw(batch, 50f * (float) Math.sin((1f - takeDamageAction) * 3.14f));
            batch.setColor(1f, 1f, 1f, 1f);
            batch.end();
            drawHpBar(position.x + dx, position.y);
        } else {
            batch.draw(texture, position.x + dx, position.y, 0 + texture.getWidth()/2, texture.getWidth()/2, texture.getWidth(), texture.getHeight(), 1, 1, (flip ? -90 : 90), 0, 0, texture.getWidth(), texture.getHeight(), flip, false);
            dmgLabel.setText("");
            batch.end();
        }


    }

    public void update(float dt) {
        if (takeDamageAction > 0) {
            takeDamageAction -= dt;
        }
        if (attackAction > 0) {
            attackAction -= dt;
        }
    }

    public void meleeAttack(AbstractUnit enemy) {
        if (hp > 0) {
            int dmg = this.strength - (int) (enemy.defence * (Math.random() - 0.5));
            if (dmg < 0) {
                dmg = 0;
            }
            this.attackAction = 1.0f;
            enemy.takeDamage(dmg);
        }
    }

    public void drawHpBar(float x, float y) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x, y + texture.getHeight(), texture.getWidth(), HP_HEIGHT);
        shapeRenderer.setColor(Color.RED);
        float percentOfHp = (float) hp / (float) maxHp;
        float sizeOfGreenBar = texture.getWidth() * percentOfHp;
        shapeRenderer.rect(x + sizeOfGreenBar,y + texture.getHeight(), texture.getWidth() - sizeOfGreenBar, HP_HEIGHT);
        shapeRenderer.end();
    }
}
