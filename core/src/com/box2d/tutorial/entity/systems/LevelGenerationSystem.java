package com.box2d.tutorial.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.box2d.tutorial.LevelFactory;
import com.box2d.tutorial.entity.components.PlayerComponent;
import com.box2d.tutorial.entity.components.TransformComponent;

public class LevelGenerationSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> tm;
    private LevelFactory lf;

    public LevelGenerationSystem(LevelFactory lvlFactory) {
        super(Family.all(PlayerComponent.class).get());
        lf = lvlFactory;
        tm = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent trans = tm.get(entity);
        int currentPosition = (int) trans.position.y;
        if ((currentPosition + 7) > lf.currentLevel) {
            lf.generateLevel(currentPosition + 7);
        }
    }
}
