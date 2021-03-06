package com.geek.rpg.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by FlameXander on 13.11.2017.
 */

public class ScreenManager {
    public enum ScreenType {
        MENU, BATTLE
    }

    private static final ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    private RpgGame rpgGame;
    private Viewport viewport;
    private BattleScreen battleScreen;

    public Viewport getViewport() {
        return viewport;
    }

    public void init(RpgGame rpgGame, SpriteBatch batch) {
        this.rpgGame = rpgGame;
        this.battleScreen = new BattleScreen(batch);
        this.viewport = new FitViewport(1280, 720);
        this.viewport.update(1280, 720, true);
        this.viewport.apply();
    }

    public void onResize(int width, int height) {
        viewport.update(width, height, true);
        viewport.apply();
    }

    private ScreenManager() {
    }

    public void switchScreen(ScreenType type) {
        Screen screen = rpgGame.getScreen();
        Assets.getInstance().clear();
        if (screen != null) {
            screen.dispose();
        }
        switch (type) {
            case BATTLE:
                Assets.getInstance().loadAssets(ScreenType.BATTLE);
                rpgGame.setScreen(battleScreen);
                break;
        }
    }

    public void dispose() {
        Assets.getInstance().clear();
        Assets.getInstance().getAssetManager().dispose();
    }
}
