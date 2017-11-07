package com.geek.rpg.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class FlyingText {
    private Vector2 position;
    private String text;
    private float time;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public FlyingText() {
        this.position = new Vector2(0, 0);
        this.text = "";
        this.time = 0.0f;
    }

    public void setup(String text, float x, float y) {
        this.active = true;
        this.position.set(x, y);
        this.text = text;
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        font.draw(batch, text, position.x, position.y);
    }

    public void update(float dt) {
        position.add(20 * dt, 60 * dt);
        time += dt;
        if (time > 1.0f) {
            time = 0.0f;
            active = false;
        }
    }
}
