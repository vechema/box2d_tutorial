package com.box2d.tutorial.entity.components;

import com.badlogic.ashley.core.Component;

public class EnemyComponent implements Component {
    public boolean isDead = false;
    public float xPosCenter = -1f;
    public boolean isGoingLeft = false;
}
