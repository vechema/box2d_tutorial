package com.box2d.tutorial;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class B2dModel {

    public World world;
    private Body bodyd;
    private Body bodys;
    private Body bodyk;
    private Body player;

    public boolean isSwimming = false;

    public B2dModel() {
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
        if(isSwimming) {
            player.applyForceToCenter(0,50,true);
        }
        world.step(delta, 3, 3);
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
