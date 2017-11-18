package com.geek.rpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private Background background;

    private List<AbstractUnit> units;
    private Hero player;
    private int currentUnit;
    private int selectedUnit;
    private Texture textureSelector;
    private List<Button> btnGUI;
    private InfoSystem infoSystem;

    private float animationTimer;

    public boolean canIMakeTurn() {
        return animationTimer <= 0.0f;
    }

    public InfoSystem getInfoSystem() {
        return infoSystem;
    }

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        background = new Background();
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        infoSystem = new InfoSystem();
        textureSelector = new Texture("selector.png");
        units = new ArrayList<AbstractUnit>();
        player = new Hero(this, new Vector2(500, 200));
        units.add(player);
        units.add(new Monster(this, new Vector2(700, 200), player));
        units.add(new Monster(this, new Vector2(800, 100), player));
        currentUnit = 0;
        selectedUnit = 0;
        prepareButtons();
        animationTimer = 0.0f;
    }

    public boolean isHeroTurn() {
        return units.get(currentUnit) instanceof Hero;
    }

    public void prepareButtons() {
        btnGUI = new ArrayList<Button>();
        Button btnAttack = new Button("Attack", new Texture("btn.png"), new Rectangle(200, 20, 80, 80)) {
            @Override
            public void action() {
                if (units.get(selectedUnit) instanceof Monster) {
                    player.meleeAttack(units.get(selectedUnit));
                    nextTurn();
                }
            }
        };
        Button btnHeal = new Button("Heal", new Texture("btn.png"), new Rectangle(300, 20, 80, 80)) {
            @Override
            public void action() {
                player.heal(0.15f);
                nextTurn();
            }
        };
        Button btnDefence = new Button("Defence", new Texture("btn.png"), new Rectangle(400, 20, 80, 80)) {
            @Override
            public void action() {
                player.defenceStance(1);
                nextTurn();
            }
        };
        Button btnRegeneration = new Button("Regeneration", new Texture("btn.png"), new Rectangle(500, 20, 80, 80)) {
            @Override
            public void action() {
                player.regenerate(3);
                nextTurn();
            }
        };
        btnGUI.add(btnAttack);
        btnGUI.add(btnHeal);
        btnGUI.add(btnDefence);
        btnGUI.add(btnRegeneration);
    }

    public void nextTurn() {
        do {
            currentUnit++;
            if (currentUnit >= units.size()) {
                currentUnit = 0;
            }
        } while (!units.get(currentUnit).isAlive());
        units.get(currentUnit).getTurn();
        animationTimer = 1.0f;
    }

    public void update(float dt) {
        if (animationTimer > 0.0f) {
            animationTimer -= dt;
        }
        for (int i = 0; i < units.size(); i++) {
            units.get(i).update(dt);
            if (InputHandler.checkClickInRect(units.get(i).getRect()) && units.get(i).isAlive()) {
                selectedUnit = i;
            }
        }
        if (isHeroTurn() && canIMakeTurn()) {
            for (int i = 0; i < btnGUI.size(); i++) {
                btnGUI.get(i).checkClick();
            }
        }
        if (!isHeroTurn()) {
            if (((Monster) units.get(currentUnit)).ai(dt)) {
                nextTurn();
            }
        }
        infoSystem.update(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        for (int i = 0; i < units.size(); i++) {
            if (currentUnit == i) {
                batch.setColor(1, 1, 0, 0.8f);
                batch.draw(textureSelector, units.get(i).getPosition().x, units.get(i).getPosition().y - 5);
            }
            if (isHeroTurn() && selectedUnit == i) {
                batch.setColor(1, 0, 0, 0.8f);
                if (units.get(i).isAlive()) {
                    batch.draw(textureSelector, units.get(i).getPosition().x, units.get(i).getPosition().y - 5);
                } else {
                    for (int j = 0; j < units.size(); j++) {
                        if (units.get(j) instanceof Monster && units.get(j).isAlive()) {
                            selectedUnit = j;
                            break;
                        }
                    }
                }
            }
            batch.setColor(1, 1, 1, 1);
            units.get(i).render(batch);
            if (units.get(i).isAlive()) {
                units.get(i).renderInfo(batch, font);
            }
        }
        if (isHeroTurn()) { //  && InputHandler.getY() < 100
            for (int i = 0; i < btnGUI.size(); i++) {
                btnGUI.get(i).render(batch);
            }
        }
        infoSystem.render(batch, font);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
