package com.geek.rpg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class RpgGame extends ApplicationAdapter {
    SpriteBatch batch;
    Background background;
    Hero hero;
    Monster monster;
    AbstractUnit currentUnit;
    ListOfUnits listOfUnits;

    @Override
    public void create() {
        listOfUnits = new ListOfUnits();
        batch = new SpriteBatch();
        background = new Background();
        listOfUnits.newHero(400, 200);
        listOfUnits.newMonster(700, 200);
        listOfUnits.newMonster(800, 100);
        currentUnit = listOfUnits.getNext();
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        batch.end();
        listOfUnits.render();
    }

    public void update(float dt) {
        listOfUnits.update(dt);
        if (currentUnit instanceof Hero) {
            for (AbstractUnit unit : listOfUnits.getListOfUnits()) {
                if (InputHandler.checkClickInRect(unit.rect) && (unit instanceof Monster)) {
                    currentUnit.meleeAttack(unit);
                    currentUnit = listOfUnits.getNext();
                    break;
                }
            }
        }
        if (currentUnit instanceof Monster) {
            if (InputHandler.checkClickInRect(listOfUnits.getHero().rect)) {
                currentUnit.meleeAttack(listOfUnits.getHero());
                currentUnit = listOfUnits.getNext();
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
