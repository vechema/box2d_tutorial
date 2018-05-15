package com.box2d.tutorial.views;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.box2d.tutorial.B2dContactListener;
import com.box2d.tutorial.B2dModel;
import com.box2d.tutorial.BodyFactory;
import com.box2d.tutorial.Box2DTutorial;
import com.box2d.tutorial.controller.KeyboardController;
import com.box2d.tutorial.entity.components.B2dBodyComponent;
import com.box2d.tutorial.entity.components.CollisionComponent;
import com.box2d.tutorial.entity.components.PlayerComponent;
import com.box2d.tutorial.entity.components.StateComponent;
import com.box2d.tutorial.entity.components.TextureComponent;
import com.box2d.tutorial.entity.components.TransformComponent;
import com.box2d.tutorial.entity.components.TypeComponent;
import com.box2d.tutorial.entity.systems.AnimationSystem;
import com.box2d.tutorial.entity.systems.CollisionSystem;
import com.box2d.tutorial.entity.systems.PhysicsDebugSystem;
import com.box2d.tutorial.entity.systems.PhysicsSystem;
import com.box2d.tutorial.entity.systems.PlayerControlSystem;
import com.box2d.tutorial.entity.systems.RenderingSystem;

public class MainScreen implements Screen {

    private Box2DTutorial parent;
    private B2dModel model;
    private OrthographicCamera cam;
    private Box2DDebugRenderer debugRenderer;
    private KeyboardController controller;
    private AtlasRegion playerTex;
    private SpriteBatch sb;
    private TextureAtlas atlas;
    private World world;
    private BodyFactory bodyFactory;
    private Sound ping;
    private Sound boing;
    private PooledEngine engine;

    public MainScreen(Box2DTutorial box2DTutorial) {
        parent = box2DTutorial;
        controller = new KeyboardController();
        world = new World(new Vector2(0,-10f),true);
        world.setContactListener(new B2dContactListener());
        bodyFactory = BodyFactory.getInstance(world);

        parent.assMan.queueAddSounds();
        parent.assMan.manager.finishLoading();
        atlas = parent.assMan.manager.get("images/game.atlas", TextureAtlas.class);
        ping = parent.assMan.manager.get("sounds/ping.wav", Sound.class);
        boing = parent.assMan.manager.get("sounds/boing.wav", Sound.class);

        sb = new SpriteBatch();
        RenderingSystem renderingSystem = new RenderingSystem(sb);
        cam = renderingSystem.getCamera();
        sb.setProjectionMatrix(cam.combined);

        engine = new PooledEngine();

        engine.addSystem(new AnimationSystem());
        engine.addSystem(renderingSystem);
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new PhysicsDebugSystem(world, renderingSystem.getCamera()));
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new PlayerControlSystem(controller));

        // create some game objects
        createPlayer();
        createPlatform(2,2);
        createPlatform(2,7);
        createPlatform(7,2);
        createPlatform(7,7);
        createFloor();
    }

    private void createPlayer() {

        // Entity + components
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);

        b2dbody.body = bodyFactory.makeCirclePolyBody(5,5,0.5f,BodyFactory.STONE, BodyType.DynamicBody, true);
        position.positon.set(10,10,0);
        texture.region = atlas.findRegion("player");
        type.type = TypeComponent.PLAYER;
        stateCom.set(StateComponent.STATE_NORMAL);
        b2dbody.body.setUserData(entity);

        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(player);
        entity.add(colComp);
        entity.add(type);
        entity.add(stateCom);

        engine.addEntity(entity);
    }

    private void createPlatform(float x, float y){
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 3, 0.2f, BodyFactory.STONE, BodyType.StaticBody);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = atlas.findRegion("player");
        TypeComponent type = engine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SCENERY;
        b2dbody.body.setUserData(entity);

        entity.add(b2dbody);
        entity.add(texture);
        entity.add(type);

        engine.addEntity(entity);
    }

    private void createFloor(){
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        b2dbody.body = bodyFactory.makeBoxPolyBody(0, 0, 100, 0.2f, BodyFactory.STONE, BodyType.StaticBody);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = atlas.findRegion("player");
        TypeComponent type = engine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SCENERY;

        b2dbody.body.setUserData(entity);

        entity.add(b2dbody);
        entity.add(texture);
        entity.add(type);

        engine.addEntity(entity);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
