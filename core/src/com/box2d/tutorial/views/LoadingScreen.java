package com.box2d.tutorial.views;

import com.badlogic.gdx.Screen;
import com.box2d.tutorial.Box2DTutorial;

public class LoadingScreen implements Screen {

    private Box2DTutorial parent;

    public LoadingScreen(Box2DTutorial box2DTutorial) {
        this.parent = box2DTutorial;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        parent.changeScreen(Box2DTutorial.MENU);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
