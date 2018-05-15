package com.box2d.tutorial.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.box2d.tutorial.entity.components.B2dBodyComponent;
import com.box2d.tutorial.entity.components.TransformComponent;

public class PhysicsSystem extends IteratingSystem {

    private static final float MAX_STEP_TIME = 1/45f;
    private static float accumulator = 0f;

    private World world;
    private Engine engine;
    private Array<Entity> bodiesQueue;

    private ComponentMapper<B2dBodyComponent> bm;
    private ComponentMapper<TransformComponent> tm;

    @SuppressWarnings("unchecked")
    public PhysicsSystem(World world, Engine eng) {
        super(Family.all(B2dBodyComponent.class, TransformComponent.class).get());
        this.world = world;
        this.bodiesQueue = new Array<Entity>();
        this.engine = eng;
        bm = ComponentMapper.getFor(B2dBodyComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;

        if (accumulator >= MAX_STEP_TIME) {
            world.step(MAX_STEP_TIME, 6, 2);
            accumulator -= MAX_STEP_TIME;

            for (Entity entity : bodiesQueue) {
                TransformComponent tfm = tm.get(entity);
                B2dBodyComponent bodyComp = bm.get(entity);
                Vector2 position = bodyComp.body.getPosition();
                tfm.position.x = position.x;
                tfm.position.y = position.y;
                tfm.rotation = bodyComp.body.getAngle() * MathUtils.radiansToDegrees;

                if(bodyComp.isDead) {
                    System.out.println("Removing a body and entity");
                    world.destroyBody(bodyComp.body);
                    engine.removeEntity(entity);
                }
            }
        }
        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}
