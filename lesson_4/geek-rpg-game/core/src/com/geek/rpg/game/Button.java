package com.geek.rpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Button {
    private String action;
    private Texture texture;
    private Rectangle rectangle;

    public String getAction() {
        return action;
    }

    public Button(String action, Texture texture, Rectangle rectangle) {
        this.action = action;
        this.texture = texture;
        this.rectangle = rectangle;
    }

    public boolean checkClick() {
        if(InputHandler.checkClickInRect(rectangle)) {
            action();
            return true;
        }
        return false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, rectangle.getX(), rectangle.getY());
    }

    public abstract void action();
}
