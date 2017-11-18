package com.geek.rpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.geek.rpg.game.actions.BaseAction;
import com.geek.rpg.game.actions.DefenceStanceAction;
import com.geek.rpg.game.actions.MeleeAttackAction;
import com.geek.rpg.game.actions.RestAction;

import java.util.ArrayList;
import java.util.List;

public class BattleScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private Background background;

    private List<Unit> units;
    private int currentUnitIndex;
    private Unit currentUnit;
    private Texture textureSelector;
    private InfoSystem infoSystem;
    private SpecialFX sfx;
    private UnitFactory unitFactory;

    public List<Unit> getUnits() {
        return units;
    }

    private Vector2[][] stayPoints;

    public Vector2[][] getStayPoints() {
        return stayPoints;
    }

    private float animationTimer;

    private Stage stage;
    private Skin skin;

    public boolean canIMakeTurn() {
        return animationTimer <= 0.0f;
    }

    public InfoSystem getInfoSystem() {
        return infoSystem;
    }

    public BattleScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    private MyInputProcessor mip;

    @Override
    public void show() {
        stayPoints = new Vector2[4][3];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int x = 280 + i * 160 + j * 20;
                if (i > 1) x += 100;
                stayPoints[i][j] = new Vector2(x, 400 - j * 120);
            }
        }
        unitFactory = new UnitFactory(this);
        units = new ArrayList<Unit>();
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.KNIGHT, this, true, 1, 0);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.KNIGHT, this, true, 1, 1);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.KNIGHT, this, true, 1, 2);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.SKELETON, this, true, 0, 0);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.SKELETON, this, true, 0, 1);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.SKELETON, this, true, 0, 2);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.SKELETON, this, false, 2, 0);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.SKELETON, this, false, 2, 1);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.SKELETON, this, false, 2, 2);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.KNIGHT, this, false, 3, 1);

        mip = new MyInputProcessor();
        Gdx.input.setInputProcessor(mip);
        background = new Background();
        font = Assets.getInstance().getAssetManager().get("font.fnt", BitmapFont.class);
        infoSystem = new InfoSystem();
        textureSelector = Assets.getInstance().getAssetManager().get("selector.png", Texture.class);

        currentUnitIndex = 0;
        currentUnit = units.get(currentUnitIndex);
        sfx = new SpecialFX();
        createGUI();
        InputMultiplexer im = new InputMultiplexer(mip, stage);
        Gdx.input.setInputProcessor(im);
        animationTimer = 0.0f;
    }

    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        skin = new Skin();
        List<BaseAction> list = unitFactory.getActions();
        for (BaseAction o : list) {
            skin.add(o.getName(), o.getBtnTexture());
            Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
            buttonStyle.up = skin.newDrawable(o.getName(), Color.WHITE);
            skin.add(o.getName(), buttonStyle);
        }
        for (Unit o : units) {
            if (!o.isAI()) {
                Group actionPanel = new Group();
                Image image = new Image(Assets.getInstance().getAssetManager().get("actionPanel.png", Texture.class));
                actionPanel.addActor(image);
                actionPanel.setPosition(1280 / 2 - 840 / 2, 5);
                actionPanel.setVisible(false);
                o.setActionPanel(actionPanel);
                stage.addActor(actionPanel);

                int counter = 0;
                for (BaseAction a : o.getActions()) {
                    final BaseAction ba = a;
                    Button btn = new Button(skin, a.getName());
                    btn.setPosition(30 + counter * 100, 30);
                    btn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (!currentUnit.isAI()) {
                                if (ba.action(currentUnit)) {
                                    nextTurn();
                                }
                            }
                        }
                    });
                    actionPanel.addActor(btn);
                    counter++;
                }
            }
        }
    }

    public boolean isHeroTurn() {
        return currentUnit.getAutopilot() == null;
    }

    public void nextTurn() {
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).getActionPanel() != null) {
                units.get(i).getActionPanel().setVisible(false);
            }
        }
        do {
            currentUnitIndex++;
            if (currentUnitIndex >= units.size()) {
                currentUnitIndex = 0;
            }
        } while (!units.get(currentUnitIndex).isAlive());
        currentUnit = units.get(currentUnitIndex);
        currentUnit.getTurn();
        animationTimer = 1.0f;
        if (currentUnit.getActionPanel() != null) {
            currentUnit.getActionPanel().setVisible(true);
        }
    }

    public void update(float dt) {
        if(isHeroTurn() && canIMakeTurn()) {
            stage.act(dt);
            if(currentUnit.getActionPanel() != null) {
                currentUnit.getActionPanel().setVisible(true);
            }
        }
        if (animationTimer > 0.0f) {
            animationTimer -= dt;
        }
        for (int i = 0; i < units.size(); i++) {
            units.get(i).update(dt);
            if (mip.isTouchedInArea(units.get(i).getRect()) && units.get(i).isAlive()) {
                currentUnit.setTarget(units.get(i));
            }
        }
        if (!isHeroTurn()) {
            if (currentUnit.getAutopilot().turn(currentUnit)) {
                nextTurn();
            }
        }
        infoSystem.update(dt);
        if (mip.isTouched()) {
            sfx.setup(mip.getX(), mip.getY());
        }
        if (sfx.isActive()) {
            sfx.update(dt);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        batch.setColor(1, 1, 0, 0.8f);
        batch.draw(textureSelector, currentUnit.getPosition().x, currentUnit.getPosition().y - 5);
        if (isHeroTurn() && currentUnit.getTarget() != null) {
            batch.setColor(1, 0, 0, 0.8f);
            batch.draw(textureSelector, currentUnit.getTarget().getPosition().x, currentUnit.getTarget().getPosition().y - 5);
        }
        batch.setColor(1, 1, 1, 1);
        for (int i = 0; i < units.size(); i++) {
            units.get(i).render(batch);
            if (units.get(i).isAlive()) {
                units.get(i).renderInfo(batch, font);
            }
        }
        infoSystem.render(batch, font);
        if (sfx.isActive()) {
            sfx.render(batch);
        }
        batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().onResize(width, height);
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
