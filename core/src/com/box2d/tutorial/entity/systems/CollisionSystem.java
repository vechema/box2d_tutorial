package com.box2d.tutorial.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.box2d.tutorial.entity.components.BulletComponent;
import com.box2d.tutorial.entity.components.CollisionComponent;
import com.box2d.tutorial.entity.components.EnemyComponent;
import com.box2d.tutorial.entity.components.Mapper;
import com.box2d.tutorial.entity.components.PlayerComponent;
import com.box2d.tutorial.entity.components.TypeComponent;

public class CollisionSystem extends IteratingSystem {

    ComponentMapper<CollisionComponent> cm;
    ComponentMapper<PlayerComponent> pm;

    @SuppressWarnings("unchecked")
    public CollisionSystem() {
        super(Family.all(CollisionComponent.class).get());

        cm = ComponentMapper.getFor(CollisionComponent.class);
        pm = ComponentMapper.getFor(PlayerComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CollisionComponent cc = cm.get(entity);
        Entity collidedEntity = cc.collisionEntity;
        TypeComponent thisType = entity.getComponent(TypeComponent.class);

        if (thisType.type == TypeComponent.PLAYER) {
            if(collidedEntity != null) {
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
                            break;
                        case TypeComponent.OTHER:
                            // player hit other
                            System.out.println("player hit other");
                            break;
                        case TypeComponent.BULLET:
                            // player hit bullet
                            System.out.println("player hit bullet, inside self");
                            break;
                        default:
                            System.out.println("No matching type found");
                    }
                    // Collision handled -> reset component
                    cc.collisionEntity = null;
                } else {
                    System.out.println("Player: collidedEntity.type == null");
                }
            }
        } else if (thisType.type == TypeComponent.ENEMY) {
            if (collidedEntity != null) {
                TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
                if(type != null){
                    switch(type.type){
                        case TypeComponent.PLAYER:
                            System.out.println("enemy hit player");
                            break;
                        case TypeComponent.ENEMY:
                            System.out.println("enemy hit enemy");
                            break;
                        case TypeComponent.SCENERY:
                            System.out.println("enemy hit scenery");
                            break;
                        case TypeComponent.SPRING:
                            System.out.println("enemy hit spring");
                            break;
                        case TypeComponent.OTHER:
                            System.out.println("enemy hit other");
                            break;
                        case TypeComponent.BULLET:
                            EnemyComponent enemy = Mapper.enemyCom.get(entity);
                            enemy.isDead = true;
                            BulletComponent bullet = Mapper.bulletCom.get(collidedEntity);
                            bullet.isDead = true;
                            System.out.println("enemy got shot");
                            break;
                        default:
                            System.out.println("No matching type found");
                    }
                    cc.collisionEntity = null; // collision handled reset component
                }else{
                    System.out.println("Enemy: collidedEntity.type == null");
                }
            }
        }
    }
}
