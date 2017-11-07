package com.geek.rpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Button {
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
        return InputHandler.checkClickInRect(rectangle);
    }

    public void render(SpriteBatch batch) {batch.draw(texture, rectangle.getX(), rectangle.getY());
    }
}
