package com.geek.rpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.geek.rpg.game.actions.BaseAction;
import com.geek.rpg.game.actions.DefenceStanceAction;
import com.geek.rpg.game.actions.MeleeAttackAction;
import com.geek.rpg.game.actions.RestAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FlameXander on 13.11.2017.
 */

public class UnitFactory {
    public enum UnitType {
        KNIGHT, SKELETON;
    }

    private BattleScreen battleScreen;
    private Map<UnitType, Unit> data;
    private List<Autopilot> aiBank;
    private List<BaseAction> actions;

    public List<BaseAction> getActions() {
        return actions;
    }

    public UnitFactory(BattleScreen battleScreen) {
        this.battleScreen = battleScreen;
        this.createActions();
        this.aiBank = new ArrayList<Autopilot>();
        this.aiBank.add(new Autopilot() {
            @Override
            public boolean turn(Unit me) {
                if (!me.getBattleScreen().canIMakeTurn()) {
                    return false;
                }
                Unit target = null;
                do {
                    target = me.getBattleScreen().getUnits().get((int) (Math.random() * me.getBattleScreen().getUnits().size()));
                } while (target.isAI());
                me.setTarget(target);
                me.getActions().get(0).action(me);
                return true;
            }
        });
        this.createUnitPatterns();
    }

    public void createActions() {
        this.actions = new ArrayList<BaseAction>();
        this.actions.add(new MeleeAttackAction());
        this.actions.add(new DefenceStanceAction());
        this.actions.add(new RestAction());
    }

    public void createUnitPatterns() {
        data = new HashMap<UnitType, Unit>();
        Stats knightStats = new Stats(1, 20, 10, 30, 2, 5, 1.0f, 0.2f, 1.0f, 0.2f, 1.0f);
        Unit knight = new Unit(Assets.getInstance().getAssetManager().get("knight.png", Texture.class), knightStats);
        knight.getActions().add(actions.get(0));
        knight.getActions().add(actions.get(1));
        data.put(UnitType.KNIGHT, knight);

        Stats skeletonStats = new Stats(1, 10, 20, 15, 1, 0, 0.5f, 1.0f, 1.0f, 0.2f, 0.1f);
        Unit skeleton = new Unit(Assets.getInstance().getAssetManager().get("skeleton.png", Texture.class), skeletonStats);
        skeleton.getActions().add(actions.get(0));
        skeleton.getActions().add(actions.get(2));
        data.put(UnitType.SKELETON, skeleton);
    }

    public Unit createUnit(UnitType unitType, boolean isHuman) {
        Unit unitPattern = data.get(unitType);
        Unit unit = new Unit(unitPattern.getTexture(), (Stats) unitPattern.getStats().clone());
        unit.setActions(unitPattern.getActions());
        if (!isHuman) {
            unit.setAutopilot(aiBank.get(0));
        }
        return unit;
    }

    public void createUnitAndAddToBattle(UnitType unitType, BattleScreen battleScreen, boolean isHuman, int x, int y) {
        Unit unit = createUnit(unitType, isHuman);
        unit.setToMap(battleScreen, x, y);
        if (!isHuman) {
            unit.setFlip(true);
        }
        battleScreen.getUnits().add(unit);
    }
}
