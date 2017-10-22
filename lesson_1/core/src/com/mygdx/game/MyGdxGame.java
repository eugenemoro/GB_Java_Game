package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Ball ball;
	static Racket racket;
	static Field field = new Field();

	static class Field {
		private int WIDTH = 1280;
		private int HEIGHT = 720;

		public boolean wallLeftRightIsHit (Ball ball) {
			float startX = ball.getbVector().x;
			int sizeX = ball.getSizeX();
			if (startX + sizeX > WIDTH || startX < 0){
				return true;
			}
			return false;
		}

		public boolean wallTopIsHit(Ball ball) {
			float startY = ball.getbVector().y;
			int	sizeY = ball.getSizeY();
			if (startY + sizeY > HEIGHT || startY < 0){
				return true;
			}
			return false;
		}
	}

	static class Ball{
		private Texture ball;
		private Vector2 bVector;
		private Vector2 spVector;
		private int sizeX = 50;
		private int sizeY = 50;

		public Ball(Texture ball, float x, float y, float vx, float vy){
			this.ball = ball;
			this.bVector = new Vector2(x, y);
			float angle = (float) Math.random();
			spVector = new Vector2((float)(Math.sqrt(1 - (angle*angle))) * vx, (angle * vx));
		}

		public void render(SpriteBatch batch){
			batch.draw(ball, bVector.x, bVector.y, sizeX, sizeY);
		}

		public void update(float dt) {
			if (field.wallLeftRightIsHit(this)){
				spVector.set(spVector.x * (-1), spVector.y * 1);
			}
			if (field.wallTopIsHit(this)){
				spVector.set(spVector.x * 1, spVector.y * (-1));
			}
			if (racket.topIsHit(this)){
				spVector.set(spVector.x * 1, spVector.y * (-1));
			}
			bVector.mulAdd(spVector, dt);
		}

		public Vector2 getbVector() {
			return bVector;
		}

		public int getSizeX() {
			return sizeX;
		}

		public int getSizeY() {
			return sizeY;
		}
	}

	static class Racket{
		private Texture racket;
		private Vector2 rVector;
		private float vx;
		private float rWidth = 100;
		private float rHeight = 100;

		public Racket(Texture racket, float x, float y, float vx){
			this.racket = racket;
			this.rVector = new Vector2(x, y);
			this.vx = vx;
		}

		public void render(SpriteBatch batch){
			batch.draw(racket, rVector.x, rVector.y, rWidth, rHeight);
		}

		public void update(float dt) {
			if (Gdx.input.isKeyPressed(Input.Keys.A)) {
				rVector.mulAdd(new Vector2((-1 * vx), 0), dt);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.D)) {
				rVector.mulAdd(new Vector2(vx, 0), dt);
			}
		}

		public boolean topIsHit(Ball ball) {
			float startX = ball.getbVector().x;
			int	sizeX = ball.getSizeX();
			float startY = ball.getbVector().y;
			int	sizeY = ball.getSizeY();
			if (((rHeight > startY) && (rVector.x < startX) && (rVector.x + rWidth > startX))
							|| ((rHeight > startY) && (rVector.x < startX + sizeX) && (rVector.x + rWidth > startX + sizeX))){
				return true;
			}
			return false;
		}
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		ball = new Ball(new Texture("Tennis_Ball.png"), 640 - 25, 360 - 25, 600, 600);
		racket = new Racket(new Texture("ping_pong.png"), 0, 0, 600);
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0, 0.6f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		update(dt);
		ball.render(batch);
		racket.render(batch);
		batch.end();
	}

	public void update(float dt){
		ball.update(dt);
		racket.update(dt);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
