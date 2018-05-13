package com.box2d.tutorial;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.box2d.tutorial.controller.KeyboardController;

public class B2dModel {

    public World world;
    private Body bodyd;
    private Body bodys;
    private Body bodyk;
    private KeyboardController controller;
    private OrthographicCamera camera;

    private Body player;
    public boolean isSwimming = false;

    public B2dModel(KeyboardController controller, OrthographicCamera camera) {
        this.camera = camera;
        this.controller = controller;
        world = new World(new Vector2(0, -10f),true);
        world.setContactListener(new B2dContactListener(this));
        createFloor();
        //createObject();
        //createMovingObject();

        // get our body factory singleton
        BodyFactory bodyFactory = BodyFactory.getInstance(world);

        // Add a player
        player = bodyFactory.makeBoxPolyBody(1,1,2,2,BodyFactory.RUBBER, BodyType.DynamicBody, false);

        // Add some water
        Body water = bodyFactory.makeBoxPolyBody(1,-8,40,4, BodyFactory.RUBBER, BodyType.StaticBody,false);
        water.setUserData("IAMTHESEA");

        // mat. the water a sensor so it doesn't obstruct our player
        bodyFactory.makeAllFixturesSensors(water);
    }
    public void logicStep(float delta) {

        if(controller.left){
            player.applyForceToCenter(-10, 0,true);
        }else if(controller.right){
            player.applyForceToCenter(10, 0,true);
        }else if(controller.up){
            player.applyForceToCenter(0, 10,true);
        }else if(controller.down){
            player.applyForceToCenter(0, -10,true);
        }

        if(controller.isMouse1Down && pointIntersectsBody(player, controller.mouseLocation)){
            System.out.println("Player was clicked");
        }

        if(isSwimming) {
            player.applyForceToCenter(0,40,true);
        }
        world.step(delta, 3, 3);
    }

    public boolean pointIntersectsBody(Body body, Vector2 mousePosition) {
        Vector3 mousePos = new Vector3(mousePosition, 0);
        camera.unproject(mousePos);
        if(body.getFixtureList().first().testPoint(mousePos.x, mousePos.y)){
            return true;
        }
        return false;
    }

    private void createObject() {
        // create a new body definition (type and location)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(0,0);

        // add it to the world
        bodyd = world.createBody(bodyDef);

        // set the shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1,1);

        // set the properties
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        // create the physical object in our body
        bodyd.createFixture(shape, 0.0f);

        shape.dispose();
    }

    private void createFloor() {
        // create new body definition (type and location)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(0,-10);

        // add it to the world
        bodys = world.createBody(bodyDef);

        // set the shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50,1);

        // create the physical object in our body
        bodys.createFixture(shape, 0.0f);

        shape.dispose();
    }

    private void createMovingObject() {
        // create new body definition (type and location)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.KinematicBody;
        bodyDef.position.set(0,-12);

        // add to the world
        bodyk = world.createBody(bodyDef);

        // set the shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1,1);

        // set the properties of the object ( shape, weight, restitution(bouncyness)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        // create the physical object in our body)
        // without this our body would just be data in the world
        bodyk.createFixture(shape, 0.0f);

        // we no longer use the shape object here so dispose of it.
        shape.dispose();

        bodyk.setLinearVelocity(0, 0.75f);
    }
}
