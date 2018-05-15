package com.box2d.tutorial.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.box2d.tutorial.entity.components.B2dBodyComponent;
import com.box2d.tutorial.entity.components.WaterFloorComponent;

public class WaterFloorSystem extends IteratingSystem {

    private Entity player;
    private ComponentMapper<B2dBodyComponent> bm;

    public WaterFloorSystem(Entity player) {
        super(Family.all(WaterFloorComponent.class).get());
        this.player = player;
        bm = ComponentMapper.getFor(B2dBodyComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        float currentyLevel = player.getComponent(B2dBodyComponent.class).body.getPosition().y;

        Body bod = bm.get(entity).body;
        float speed = (currentyLevel / 300);
        speed = speed > 1 ? 1 : speed;
        bod.setTransform(bod.getPosition().x, bod.getPosition().y + speed, bod.getAngle());
    }
}
