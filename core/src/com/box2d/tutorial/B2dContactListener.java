package com.box2d.tutorial;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class B2dContactListener implements ContactListener {

    private B2dModel parent;

    public B2dContactListener(B2dModel parent) {
        this.parent = parent;
    }

    @Override
    public void beginContact(Contact contact) {
        System.out.println("Begin contact");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        System.out.println(fa.getBody().getType() + " has hit " + fb.getBody().getType());

        // check if in water
        Object faUserData = fa.getBody().getUserData();
        Object fbUserData = fb.getBody().getUserData();

        if(faUserData != null && faUserData.equals("IAMTHESEA")){
            parent.isSwimming = true;
            return;
        }else if(fbUserData != null && fbUserData.equals("IAMTHESEA")){
            parent.isSwimming = true;
            return;
        }

        if(fa.getBody().getType() == BodyType.StaticBody){
            this.shootUpInAir(fa, fb);
        }else if(fb.getBody().getType() == BodyType.StaticBody){
            this.shootUpInAir(fb, fa);
        }else{
            // neither a nor b are static so do nothing
        }

        System.out.println();
    }

    private void shootUpInAir(Fixture staticFixture, Fixture otherFixture) {
        System.out.println("Adding force");
        otherFixture.getBody().applyForceToCenter(new Vector2(-100000, -100000), true);
    }

    @Override
    public void endContact(Contact contact) {
        System.out.println("End contact");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        // Check if in water
        Object faUserData = fa.getBody().getUserData();
        Object fbUserData = fb.getBody().getUserData();

        if(faUserData != null && faUserData.equals("IAMTHESEA")){
            parent.isSwimming = false;
            return;
        }else if(fbUserData != null && fbUserData.equals("IAMTHESEA")){
            parent.isSwimming = false;
            return;
        }

        System.out.println();
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
