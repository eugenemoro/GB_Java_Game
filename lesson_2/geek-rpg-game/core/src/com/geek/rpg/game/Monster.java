package com.geek.rpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Monster extends AbstractUnit {
    public Monster() {
        this.texture = new Texture("charSkeleton.tga");
        this.name = "Skelet";
        this.maxHp = 10;
        this.hp = this.maxHp;
        this.level = 1;
        this.strength = 5;
        this.dexterity = 5;
        this.endurance = 5;
        this.spellpower = 0;
        this.defence = 1;
        this.flip = true;
    }
}
