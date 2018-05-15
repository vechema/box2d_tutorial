package com.box2d.tutorial.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

public class CollisionComponent implements Component, Poolable {
    public Entity collisionEntity;

    @Override
    public void reset() {
        collisionEntity = null;
    }
}
