package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.components.CharacterComponent;
import com.mygdx.game.components.ModelComponent;
import com.mygdx.game.managers.EntityFactory;
import com.mygdx.game.systems.BulletSystem;
import com.mygdx.game.systems.PlayerSystem;
import com.mygdx.game.systems.RenderSystem;

public class GameWorld {
    private static final float FOV = 67F;
    private ModelBatch modelBatch;
    private ModelBatch shadowBatch;
    private Environment environment;
    private DirectionalShadowLight shadowLight;
    private PerspectiveCamera perspectiveCamera;
    private Engine engine;
    private Entity entity;
    private Entity character;

    private ModelBuilder modelBuilder = new ModelBuilder();
    private Material boxMaterial = new Material(ColorAttribute.createDiffuse(Color.WHITE),
            ColorAttribute.createSpecular(Color.RED),
            FloatAttribute.createShininess(16f));
    private Model box = modelBuilder.createBox(5, 5, 5, boxMaterial,
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    public Model model;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public AssetManager assetManager;
    public boolean loading;
    private float x = 100;
    private float y = 0;
    static boolean start = false;

    public BulletSystem bulletSystem;

    Model wallHorizontal = modelBuilder.createBox(80, 20, 1,
            new Material(ColorAttribute.createDiffuse(Color.GRAY),
                    ColorAttribute.createSpecular(Color.GRAY), FloatAttribute
                    .createShininess(16f)), VertexAttributes.Usage.Position
                    | VertexAttributes.Usage.Normal);
    Model wallVertical = modelBuilder.createBox(1, 20, 80,
            new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY),
                    ColorAttribute.createSpecular(Color.GRAY),
                    FloatAttribute.createShininess(16f)),
            VertexAttributes.Usage.Position |
                    VertexAttributes.Usage.Normal);
    Model groundModel = modelBuilder.createBox(500, 1, 1000,
            new Material(ColorAttribute.createDiffuse(Color.WHITE),
                    ColorAttribute.createSpecular(Color.WHITE),
                    FloatAttribute.createShininess(16f)),
            VertexAttributes.Usage.Position
                    | VertexAttributes.Usage.Normal);


    public GameWorld() {
        Bullet.init();
        initEnvironment();
        initModelBatch();
        initPersCamera();
        addSystems();
        addEntities();
        addModels();
    }

    private void addModels() {
        assetManager = new AssetManager();
        assetManager.load("models/IPSC_paper.obj", Model.class);
        assetManager.load("models/IPSC_plate_300.obj", Model.class);
        assetManager.load("models/IPSC_popper_850.obj", Model.class);
        assetManager.load("models/USPSA_metric.obj", Model.class);
        loading = true;
    }

    private void doneLoading() {
        Model model1 = assetManager.get("models/IPSC_paper.obj", Model.class);
        Model model2 = assetManager.get("models/IPSC_plate_300.obj", Model.class);
        Model model3 = assetManager.get("models/IPSC_popper_850.obj", Model.class);
        Model model4 = assetManager.get("models/USPSA_metric.obj", Model.class);

        Model parapet = modelBuilder.createCapsule(
                10f, 80f, 16,
                new Material(ColorAttribute.createDiffuse(Color.BROWN),
                        ColorAttribute.createSpecular(Color.BROWN),
                        FloatAttribute.createShininess(16f)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
                        | VertexAttributes.Usage.TextureCoordinates);

        ModelInstance modelInstance1 = new ModelInstance(model1);
        ModelInstance modelInstance2 = new ModelInstance(model2);
        ModelInstance modelInstance3 = new ModelInstance(model3);
        ModelInstance modelInstance4 = new ModelInstance(model4);
        ModelInstance modelInstanceParapet = new ModelInstance(parapet);

        modelInstance1.transform.scale(10.0f, 10.0f, 10.0f);
        modelInstance1.transform.setTranslation(-10.0f, 10.0f, -30.0f);
//        modelInstance1.transform.setToRotation(Vector3.X, 10);

        modelInstance2.transform.scale(300.0f, 300.0f, 300.0f);
        modelInstance2.transform.setTranslation(-5.0f, 10.0f, -30.0f);

        modelInstance3.transform.scale(850.0f, 850.0f, 850.0f);
        modelInstance3.transform.setTranslation(5.0f, 10.0f, -30.0f);

        modelInstance4.transform.scale(10.0f, 10.0f, 10.0f);
        modelInstance4.transform.setTranslation(10.0f, 10.0f, -30.0f);

        modelInstanceParapet.transform.setToRotation(Vector3.Z, 90);
        modelInstanceParapet.calculateTransforms();

        instances.add(modelInstance1);
        instances.add(modelInstance2);
        instances.add(modelInstance3);
        instances.add(modelInstance4);
        instances.add(modelInstanceParapet);

        loading = false;
    }

    private void addEntities() {
        createGround();
        createPlayer(5, 3, 5);
    }

    private void createGround() {

        engine.addEntity(EntityFactory.createStaticEntity
                (groundModel,0, 0, 0));
        engine.addEntity(EntityFactory.createStaticEntity
                (wallHorizontal, 0, 10, -40));
        engine.addEntity(EntityFactory.createStaticEntity
                (wallHorizontal, 0, 10, 40));
        engine.addEntity(EntityFactory.createStaticEntity
                (wallVertical, 40, 10, 0));
        engine.addEntity(EntityFactory.createStaticEntity
                (wallVertical, -40, 10, 0));
    }

    private void createPlayer(float x, float y, float z) {
        character = EntityFactory.createPlayer(bulletSystem, x, y, z);
        engine.addEntity(character);
    }

    private void addSystems() {
        engine = new Engine();
        engine.addSystem(new RenderSystem(modelBatch, environment));
        engine.addSystem(bulletSystem = new BulletSystem());
        engine.addSystem(new PlayerSystem(this, perspectiveCamera));
    }
    public void render(float delta) {
        renderWorld(delta);
    }
    protected void renderWorld(float delta) {
        if (loading && assetManager.update())
            doneLoading();


        modelBatch.begin(perspectiveCamera);
//        modelBatch.render(instance);
        modelBatch.render(instances, environment);
        engine.update(delta);
        modelBatch.end();
        if (start) {

            if (x > 10f) {
                x -= 0.5f;
            }
            if (y < 10f) {
                y += 0.5f;
            }

            perspectiveCamera.position.set(0f, x, x);
            perspectiveCamera.lookAt(0f, y, 0f);
        }

        perspectiveCamera.update();

        //create shadow texture
        shadowLight.begin(Vector3.Zero, perspectiveCamera.direction);
        shadowBatch.begin(shadowLight.getCamera());
        shadowBatch.render(instances, environment);
//        shadowBatch.render(instance);
        shadowBatch.end();
        shadowLight.end();
    }

    private void initPersCamera() {
        perspectiveCamera = new PerspectiveCamera(FOV, Core.VIRTUAL_WIDTH, Core.VIRTUAL_HEIGHT);
        perspectiveCamera.position.set(0f, 100f, 100f);
        //вид сверху
//        perspectiveCamera.position.set(0f, 100f, 0f);

        perspectiveCamera.lookAt(0f, 0f, 0f);
        perspectiveCamera.near = 1f;
        perspectiveCamera.far = 300f;
        perspectiveCamera.update();
    }

    private void initEnvironment() {
        environment = new Environment();
        // рассеянный свет
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));

        //направленный свет
//        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 10f, 10f, 20f));

        environment.add((shadowLight = new DirectionalShadowLight(1600, 1600, 60f, 60f, .1f, 500f))
                .set(0.4f, 0.4f, 0.4f, 5.0f, -5f, -20f));
        environment.shadowMap = shadowLight;

        engine = new Engine();
        engine.addSystem(new RenderSystem(modelBatch, environment));
        entity = new Entity();
        entity.add(new ModelComponent(box,10,10,10));
        engine.addEntity(entity);
    }

    private void initModelBatch() {
        modelBatch = new ModelBatch();
        shadowBatch = new ModelBatch(new DepthShaderProvider());
    }

    public void dispose() {
        bulletSystem.dispose();
        bulletSystem = null;
        wallHorizontal.dispose();
        wallVertical.dispose();
        groundModel.dispose();
        modelBatch.dispose();
        modelBatch = null;
        bulletSystem.collisionWorld.removeAction(character.getComponent
                (CharacterComponent.class).characterController);
        bulletSystem.collisionWorld.removeCollisionObject
                (character.getComponent(CharacterComponent.class).ghostObject);
        character.getComponent(CharacterComponent.class)
                .characterController.dispose();
        character.getComponent(CharacterComponent.class)
                .ghostObject.dispose();
        character.getComponent(CharacterComponent.class)
                .ghostShape.dispose();
        model.dispose();
        assetManager.dispose();
    }

    /*With the camera set we can now fill in the resize function as well*/
    public void resize(int width, int height) {
        perspectiveCamera.viewportHeight = height;
        perspectiveCamera.viewportWidth = width;
    }

}
