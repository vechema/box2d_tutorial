package com.box2d.tutorial.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.box2d.tutorial.entity.components.TextureComponent;
import com.box2d.tutorial.entity.components.TransformComponent;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem {

    // Amount of pixels each meter of box2d objects contains
    static final float PPM = 32.0f;

    // this gets the height and width of our camera frustrum based off the width and height of the screen and our pixel per meter ratio
    static final float FRUSTUM_WIDTH = Gdx.graphics.getWidth()/PPM;
    static final float FRUSTUM_HEIGHT = Gdx.graphics.getHeight()/PPM;

    // get the ratio for converting pixels to metres
    public static final float PIXELS_TO_METRES = 1.0f / PPM;

    private static Vector2 meterDimensions = new Vector2();
    private static Vector2 pixelDimensions = new Vector2();

    // static method to get screen width in metres
    public static Vector2 getScreenSizeInMeters(){
        meterDimensions.set(Gdx.graphics.getWidth()*PIXELS_TO_METRES,
                Gdx.graphics.getHeight()*PIXELS_TO_METRES);
        return meterDimensions;
    }

    // static method to get screen size in pixels
    public static Vector2 getScreenSizeInPixesl(){
        pixelDimensions.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return pixelDimensions;
    }

    // convenience method to convert pixels to meters
    public static float PixelsToMeters(float pixelValue){
        return pixelValue * PIXELS_TO_METRES;
    }

    private SpriteBatch batch;
    // Allow sorting of images -> draw images on top of each other
    private Array<Entity> renderQueue;
    // Sort images based on z position
    private Comparator<Entity> comparator;
    private OrthographicCamera cam;

    // Get components from entities
    private ComponentMapper<TextureComponent> textureM;
    private ComponentMapper<TransformComponent> transformM;

    @SuppressWarnings("unchecked")
    public RenderingSystem(SpriteBatch batch) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get(), new ZComparator());

        textureM = ComponentMapper.getFor(TextureComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);

        renderQueue = new Array<Entity>();

        this.batch = batch;

        // set up camera to match screen size
        cam = new OrthographicCamera(FRUSTUM_WIDTH,FRUSTUM_HEIGHT);
        cam.position.set(FRUSTUM_WIDTH / 2f, FRUSTUM_HEIGHT / 2f, 0);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        renderQueue.sort(comparator);

        // camera and spritebatch
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.enableBlending();
        batch.begin();

        for (Entity entity : renderQueue) {
            TextureComponent tex = textureM.get(entity);
            TransformComponent t = transformM.get(entity);

            if(tex.region == null || t.isHidden) {
                continue;
            }

            float width = tex.region.getRegionWidth();
            float height = tex.region.getRegionHeight();

            float originX = width/2f;
            float originY = height/2f;

            batch.draw(tex.region,
                    t.positon.x - originX, t.positon.y - originY,
                    originX, originY,
                    width, height,
                    PixelsToMeters(t.scale.x), PixelsToMeters(t.scale.y),
                    t.rotation);
        }
        batch.end();
        renderQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    public OrthographicCamera getCamera() {
        return cam;
    }
}
