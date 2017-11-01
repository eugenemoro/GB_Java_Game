package com.geek.rpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Hero extends AbstractUnit {
    public Hero() {
        this.texture = new Texture("charTank.tga");
        this.name = "Alexander";
        this.maxHp = 100;
        this.hp = this.maxHp;
        this.level = 1;
        this.strength = 10;
        this.dexterity = 10;
        this.endurance = 10;
        this.spellpower = 10;
        this.defence = 5;
        this.flip = false;
    }
}
