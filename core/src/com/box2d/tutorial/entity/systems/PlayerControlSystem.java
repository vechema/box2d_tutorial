package com.box2d.tutorial.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.box2d.tutorial.LevelFactory;
import com.box2d.tutorial.controller.KeyboardController;
import com.box2d.tutorial.entity.components.B2dBodyComponent;
import com.box2d.tutorial.entity.components.PlayerComponent;
import com.box2d.tutorial.entity.components.StateComponent;

public class PlayerControlSystem extends IteratingSystem {

    private LevelFactory lvlFactory;
    ComponentMapper<PlayerComponent> pm;
    ComponentMapper<B2dBodyComponent> bodm;
    ComponentMapper<StateComponent> sm;
    KeyboardController controller;

    @SuppressWarnings("unchecked")
    public PlayerControlSystem(KeyboardController keyCon, LevelFactory lvlf) {
        super(Family.all(PlayerComponent.class).get());
        controller = keyCon;
        lvlFactory = lvlf;
        pm = ComponentMapper.getFor(PlayerComponent.class);
        bodm = ComponentMapper.getFor(B2dBodyComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
    }

    @Override
    protected  void processEntity(Entity entity, float deltaTime) {
        B2dBodyComponent b2body = bodm.get(entity);
        StateComponent state = sm.get(entity);
        PlayerComponent player = pm.get(entity);
        //player.cam.position.y = b2body.body.getPosition().y;

        if (b2body.body.getLinearVelocity().y > 0) {
            state.set(StateComponent.STATE_FALLING);
        }

        if (b2body.body.getLinearVelocity().y == 0) {
            if(state.get() == StateComponent.STATE_FALLING) {
                state.set(StateComponent.STATE_NORMAL);
            }
            if (b2body.body.getLinearVelocity().x != 0) {
                state.set(StateComponent.STATE_MOVING);
            }
        }

        if(b2body.body.getLinearVelocity().y < 0 && state.get() == StateComponent.STATE_FALLING){
            // player is actually falling. check if they are on platform
            if(player.onPlatform){
                //overwrite old y value with 0 t stop falling but keep x vel
                b2body.body.setLinearVelocity(b2body.body.getLinearVelocity().x, 0f);
            }
        }

        if (player.onSpring) {
            b2body.body.applyLinearImpulse(0,175f,b2body.body.getWorldCenter().x,
                    b2body.body.getWorldCenter().y,true);
            state.set(StateComponent.STATE_JUMPING);
            player.onSpring = false;
        }

        if(controller.left) {
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x,
                    -5f, 0.2f), b2body.body.getLinearVelocity().y);
        }
        if (controller.right) {
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x,
                    5f, 0.2f),b2body.body.getLinearVelocity().y);
        }
        if(!controller.left && ! controller.right) {
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x,
                    0, 0.1f), b2body.body.getLinearVelocity().y);
        }

        if (controller.up && (state.get() == StateComponent.STATE_NORMAL ||
            state.get() == StateComponent.STATE_MOVING)) {
            b2body.body.applyLinearImpulse(0,75f,b2body.body.getWorldCenter().x,
                    b2body.body.getWorldCenter().y,true);
            state.set(StateComponent.STATE_JUMPING);
            player.onPlatform = false;
            player.onSpring = false;
        }

        if(player.timeSinceLastShot > 0){
            player.timeSinceLastShot -= deltaTime;
        }

        if (controller.isMouse1Down) {
            System.out.println("Player shot, time since: " + player.timeSinceLastShot);
            if(player.timeSinceLastShot <= 0) {
                Vector3 mousePos = new Vector3(controller.mouseLocation.x,controller.mouseLocation.y,0);

                //Convert position from screen to box2d
                player.cam.unproject(mousePos);

                float speed = 10f;
                float shooterX = b2body.body.getPosition().x;
                float shooterY = b2body.body.getPosition().y;
                float velx = mousePos.x - shooterX;
                float vely = mousePos.y - shooterY;

                // distance to target direct
                float length = (float) Math.sqrt(velx * velx + vely * vely);

                if (length != 0) {
                    velx = velx / length;
                    vely = vely / length;
                }

                lvlFactory.createBullet(shooterX,shooterY,velx*speed, vely*speed);
                player.timeSinceLastShot = player.shootDelay;
            }
        }

    }
}
