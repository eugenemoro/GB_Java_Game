package com.geek.rpg.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by FlameXander on 13.11.2017.
 */

public class Assets {
    private static final Assets ourInstance = new Assets();

    public static Assets getInstance() {
        return ourInstance;
    }

    private AssetManager assetManager;

    public AssetManager getAssetManager() {
        return assetManager;
    }

    private Assets() {
        assetManager = new AssetManager();
    }

    public void loadAssets(ScreenManager.ScreenType type) {
        switch (type) {
            case BATTLE:
                assetManager.load("background.png", Texture.class);
                assetManager.load("knight.png", Texture.class);
                assetManager.load("skeleton.png", Texture.class);
                assetManager.load("btnMeleeAttack.png", Texture.class);
                assetManager.load("btnHeal.png", Texture.class);
                assetManager.load("btnDefence.png", Texture.class);
                assetManager.load("selector.png", Texture.class);
                assetManager.load("actionPanel.png", Texture.class);
                assetManager.load("font.fnt", BitmapFont.class);
                assetManager.finishLoading();
                break;
        }
    }

    public void clear() {
        assetManager.clear();
    }
}
