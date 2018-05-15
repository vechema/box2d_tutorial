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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.box2d.tutorial.B2dContactListener;
import com.box2d.tutorial.BodyFactory;
import com.box2d.tutorial.Box2DTutorial;
import com.box2d.tutorial.LevelFactory;
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
import com.box2d.tutorial.entity.systems.LevelGenerationSystem;
import com.box2d.tutorial.entity.systems.PhysicsDebugSystem;
import com.box2d.tutorial.entity.systems.PhysicsSystem;
import com.box2d.tutorial.entity.systems.PlayerControlSystem;
import com.box2d.tutorial.entity.systems.RenderingSystem;

public class MainScreen implements Screen {

    private Box2DTutorial parent;
    private OrthographicCamera cam;
    private KeyboardController controller;
    private SpriteBatch sb;
    private PooledEngine engine;
    private LevelFactory lvlFactory;

    private Sound ping;
    private Sound boing;
    private TextureAtlas atlas;

    public MainScreen(Box2DTutorial box2DTutorial) {
        parent = box2DTutorial;
        parent.assMan.queueAddSounds();
        parent.assMan.manager.finishLoading();
        atlas = parent.assMan.manager.get("images/game.atlas", TextureAtlas.class);
        ping = parent.assMan.manager.get("sounds/ping.wav", Sound.class);
        boing = parent.assMan.manager.get("sounds/boing.wav", Sound.class);
        controller = new KeyboardController();
        engine = new PooledEngine();
        lvlFactory = new LevelFactory(engine, atlas.findRegion("player"));

        sb = new SpriteBatch();
        RenderingSystem renderingSystem = new RenderingSystem(sb);
        cam = renderingSystem.getCamera();
        sb.setProjectionMatrix(cam.combined);

        engine.addSystem(new AnimationSystem());
        engine.addSystem(renderingSystem);
        engine.addSystem(new PhysicsSystem(lvlFactory.world));
        engine.addSystem(new PhysicsDebugSystem(lvlFactory.world, renderingSystem.getCamera()));
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new PlayerControlSystem(controller));
        engine.addSystem(new LevelGenerationSystem(lvlFactory));

        // create some game objects
        lvlFactory.createPlayer(atlas.findRegion("player"),cam);
        lvlFactory.createFloor(atlas.findRegion("player"));
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
