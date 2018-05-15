package com.box2d.tutorial.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.box2d.tutorial.entity.components.B2dBodyComponent;
import com.box2d.tutorial.entity.components.BulletComponent;
import com.box2d.tutorial.entity.components.Mapper;

public class BulletSystem extends IteratingSystem {

    private Entity player;

    @SuppressWarnings("unchecked")
    public BulletSystem(Entity player){
        super(Family.all(BulletComponent.class).get());
        this.player = player;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //get box 2d body and bullet components
        B2dBodyComponent b2body = Mapper.b2dCom.get(entity);
        BulletComponent bullet = Mapper.bulletCom.get(entity);

        // apply bullet velocity to bullet body
        b2body.body.setLinearVelocity(bullet.xVel, bullet.yVel);

        // get player pos
        B2dBodyComponent playerBodyComp = Mapper.b2dCom.get(player);
        float px = playerBodyComp.body.getPosition().x;
        float py = playerBodyComp.body.getPosition().y;

        // get bullet pos
        float bx = b2body.body.getPosition().x;
        float by = b2body.body.getPosition().y;

        if (bx - px > 20 || by - py > 20) {
            System.out.println("~~Bullet too far away");
            bullet.isDead = true;
        }

        //check if bullet is dead
        if(bullet.isDead){
            System.out.println("Bullet died");
            b2body.isDead = true;
        }
    }
}
