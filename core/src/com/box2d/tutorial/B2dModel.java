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

    public B2dModel() {
        world = new World(new Vector2(0, -10f),true);
        createFloor();
        createObject();
        createMovingObject();

        // get our body factory singleton
        BodyFactory bodyFactory = BodyFactory.getInstance(world);

        // Add rubber ball at 1, 1 (middle)
        bodyFactory.makeCirclePolyBody(1,1,1,BodyFactory.RUBBER, BodyType.DynamicBody, false);

        // Add steel ball at 4, 1 (right)
        bodyFactory.makeCirclePolyBody(4,1,1,BodyFactory.STEEL, BodyType.DynamicBody,false);

        // add stone ball at -4, 1 (left)
        bodyFactory.makeCirclePolyBody(-4,1,1,BodyFactory.STONE, BodyType.DynamicBody);

        // add wood wall
        bodyFactory.makeBoxPolyBody(5,5,2,4,BodyFactory.WOOD, BodyType.StaticBody, false);

        // Add polygon/pentagon platform
        Vector2[] vertices = new Vector2[5];
        vertices[0] = new Vector2(1,0);
        vertices[1] = new Vector2(0,3);
        vertices[2] = new Vector2(2.5f,5);
        vertices[3] = new Vector2(5,3);
        vertices[4] = new Vector2(4,0);
        bodyFactory.makePolygonShapeBody(vertices, -3, 4,BodyFactory.STONE,BodyType.KinematicBody);
    }
    public void logicStep(float delta) {
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
