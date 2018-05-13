package com.box2d.tutorial.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class KeyboardController implements InputProcessor {

    public boolean left,right,up,down;
    public boolean isMouse1Down, isMouse2Down, isMouse3Down;
    public boolean isDragged;
    public Vector2 mouseLocation = new Vector2();

    @Override
    public boolean keyDown(int keycode) {

        boolean keyPressed = false;

        switch (keycode) {
            case Keys.LEFT:
                left = true;
                keyPressed = true;
                break;
            case Keys.RIGHT:
                right = true;
                keyPressed = true;
                break;
            case Keys.UP:
                up = true;
                keyPressed = true;
                break;
            case Keys.DOWN:
                down = true;
                keyPressed = true;
                break;
        }

        return keyPressed;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean keyPressed = false;

        switch (keycode) {
            case Keys.LEFT:
                left = false;
                keyPressed = true;
                break;
            case Keys.RIGHT:
                right = false;
                keyPressed = true;
                break;
            case Keys.UP:
                up = false;
                keyPressed = true;
                break;
            case Keys.DOWN:
                down = false;
                keyPressed = true;
                break;
        }

        return keyPressed;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            isMouse1Down = true;
        } else if (button == 1) {
            isMouse2Down = true;
        } else if (button == 2) {
            isMouse3Down = true;
        }

        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDragged = false;

        if(button == 0){
            isMouse1Down = false;
        } else if (button == 1) {
            isMouse2Down = false;
        } else if (button == 2) {
            isMouse3Down = false;
        }

        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        isDragged = true;
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
