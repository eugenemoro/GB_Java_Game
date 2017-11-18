package com.geek.rpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Monster extends AbstractUnit {
    private Hero target;

    public Monster(GameScreen game, Vector2 position, Hero target) {
        super(game, position, new Texture("skeleton.png"));
        this.target = target;
        this.name = "Skelet";
        this.maxHp = 50;
        this.hp = this.maxHp;
        this.level = 1;
        this.strength = 12;
        this.dexterity = 5;
        this.endurance = 5;
        this.spellpower = 0;
        this.defence = 1;
        this.flip = true;
    }

    public boolean ai(float dt) {
        if (!game.canIMakeTurn()) return false;
        meleeAttack(target);
        return true;
    }
}
