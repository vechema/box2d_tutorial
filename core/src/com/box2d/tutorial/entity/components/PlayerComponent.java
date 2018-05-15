package com.box2d.tutorial.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class PlayerComponent implements Component {
    public OrthographicCamera cam;
    public boolean isDead = false;
    public boolean onPlatform = false;
    public boolean onSpring = false;
}
