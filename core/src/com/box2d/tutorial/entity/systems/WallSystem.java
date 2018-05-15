package com.box2d.tutorial.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.box2d.tutorial.entity.components.B2dBodyComponent;
import com.box2d.tutorial.entity.components.WallComponent;

public class WallSystem extends IteratingSystem {

    private Entity player;
    private ComponentMapper<B2dBodyComponent> bm;

    public WallSystem(Entity player) {
        super(Family.all(WallComponent.class).get());
        this.player = player;
        bm = ComponentMapper.getFor(B2dBodyComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // get current y level of player entity
        float currentyLevel = player.getComponent(B2dBodyComponent.class).body.getPosition().y;
        // get the body component of the wall we're updating
        Body bod = bm.get(entity).body;
        //set the walls y position to match the player
        bod.setTransform(bod.getPosition().x, currentyLevel, bod.getAngle());
    }
}
