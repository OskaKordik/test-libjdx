package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.utils.HelperClass;

public class Screen2D extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	OrthographicCamera camera;
	int x = 0;
	int y = 0;
	int xLine = 600;
	int yLine = 200;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Gdx.graphics.getWidth() / 2,Gdx.graphics.getHeight() / 2,0);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		if (Gdx.input.isTouched()) {
//            System.out.println("Touched! **** " + Gdx.input.getX() + " / " + Gdx.input.getY());

			if (Gdx.input.getX() > 0 && Gdx.input.getX() < 256) {
				movedSprite();
			} else {
				drawLineTouched();
			}
        } else {
			batch.draw(img, x, y);
			saveLinePosition();
		}

		batch.end();
	}

	private void saveLinePosition() {
		// save line new position
		xLine = Gdx.input.getX();
		yLine = Gdx.graphics.getHeight() - Gdx.input.getY();

		HelperClass.DrawDebugLine(
				new Vector2(Gdx.graphics.getWidth()-200,200),
				new Vector2(xLine, yLine), camera.combined
		);
	}

	private void drawLineTouched() {
		HelperClass.DrawDebugLine(
				new Vector2(Gdx.graphics.getWidth()-200,200),
				new Vector2(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY()), camera.combined
		);
	}

	private void movedSprite() {
		// в libgdx y 0 находится внизу, в android вверху
		// 128 - середина изображения
		batch.draw(img, Gdx.input.getX() - 128, Gdx.graphics.getHeight() - Gdx.input.getY() - 128);
		// save sprite new position
		x = Gdx.input.getX() - 128;
		y = Gdx.graphics.getHeight() - Gdx.input.getY() - 128;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
