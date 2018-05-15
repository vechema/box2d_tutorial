package com.box2d.tutorial.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.box2d.tutorial.entity.components.CollisionComponent;
import com.box2d.tutorial.entity.components.PlayerComponent;
import com.box2d.tutorial.entity.components.TypeComponent;

public class CollisionSystem extends IteratingSystem {

    ComponentMapper<CollisionComponent> cm;
    ComponentMapper<PlayerComponent> pm;

    @SuppressWarnings("unchecked")
    public CollisionSystem() {
        // only need to worry about player collisions
        super(Family.all(CollisionComponent.class, PlayerComponent.class).get());

        cm = ComponentMapper.getFor(CollisionComponent.class);
        pm = ComponentMapper.getFor(PlayerComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CollisionComponent cc = cm.get(entity);

        Entity collidedEntity = cc.collisionEntity;
        if (collidedEntity != null) {
            TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
            if (type != null) {
                switch (type.type) {
                    case TypeComponent.ENEMY:
                        // player hit enemy
                        System.out.println("player hit enemy");
                        PlayerComponent pl = pm.get(entity);
                        pl.isDead = true;
                        int score = (int) pl.cam.position.y;
                        System.out.println("Score = " + score);
                        break;
                    case TypeComponent.SCENERY:
                        // player hit scenery
                        System.out.println("player hit scenery");
                        pm.get(entity).onPlatform = true;
                        break;
                    case TypeComponent.SPRING:
                        // player hit spring
                        pm.get(entity).onSpring = true;
                    case TypeComponent.OTHER:
                        // player hit other
                        System.out.println("player hit other");
                        break;
                    default:
                        System.out.println("No matching type found");
                }
                // Collision handled -> reset component
                cc.collisionEntity = null;
            } else {
                System.out.println("type == null");
            }
        }
    }
}
