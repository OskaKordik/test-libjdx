package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.screens.GameScreen;

public class Core extends ApplicationAdapter {
    public static final float VIRTUAL_WIDTH = 900;
    public static final float VIRTUAL_HEIGHT = 1600;
    Screen screen;

    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        setScreen(new GameScreen(this));
    }
    @Override
    public void render() {
        Gdx.gl.glClearColor(0.63686f, 0.76436f, 0.92286f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT |
                GL20.GL_DEPTH_BUFFER_BIT);
        screen.render(Gdx.graphics.getDeltaTime());
    }
    @Override
    public void resize(int width, int height) {
        screen.resize(width, height);
    }
    public void setScreen(Screen screen) {
        if (this.screen != null) {
            this.screen.hide();
            this.screen.dispose();
        }
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
            this.screen.resize(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
        }
    }
}
