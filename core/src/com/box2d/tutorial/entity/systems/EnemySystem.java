package com.box2d.tutorial.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.box2d.tutorial.entity.components.B2dBodyComponent;
import com.box2d.tutorial.entity.components.EnemyComponent;

public class EnemySystem extends IteratingSystem {
    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<B2dBodyComponent> bodm;

    @SuppressWarnings("unchecked")
    public EnemySystem() {
        super(Family.all(EnemyComponent.class).get());
        em = ComponentMapper.getFor(EnemyComponent.class);
        bodm = ComponentMapper.getFor(B2dBodyComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemyCom = em.get(entity);
        B2dBodyComponent bodyCom = bodm.get(entity);

        // Distance of enemy from start position
        float disFromOrig = Math.abs(enemyCom.xPosCenter - bodyCom.body.getPosition().x);
        // Go other way if distance > 1
        enemyCom.isGoingLeft = disFromOrig > 1 ? !enemyCom.isGoingLeft : enemyCom.isGoingLeft;

        float speed = enemyCom.isGoingLeft ? -0.01f : 0.01f;
        bodyCom.body.setTransform(bodyCom.body.getPosition().x + speed,
                bodyCom.body.getPosition().y,
                bodyCom.body.getAngle());

        if(enemyCom.isDead) {
            bodyCom.isDead = true;
        }
    }
}
