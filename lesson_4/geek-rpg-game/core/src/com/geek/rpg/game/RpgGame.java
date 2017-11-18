package com.geek.rpg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class RpgGame extends Game {
    private SpriteBatch batch;
    private GameScreen gameScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gameScreen = new GameScreen(batch);
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        this.getScreen().render(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
        gameScreen.dispose();
    }
}