package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.mygdx.game.components.CharacterComponent;
import com.mygdx.game.components.ModelComponent;
import com.mygdx.game.managers.EntityFactory;
import com.mygdx.game.systems.BulletSystem;
import com.mygdx.game.systems.PlayerSystem;
import com.mygdx.game.systems.RenderSystem;

public class GameWorld {
    private static final float FOV = 67F;
    private ModelBatch modelBatch;
    private Environment environment;
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

    public BulletSystem bulletSystem;

    Model wallHorizontal = modelBuilder.createBox(80, 20, 1,
            new Material(ColorAttribute.createDiffuse(Color.WHITE),
                    ColorAttribute.createSpecular(Color.RED), FloatAttribute
                    .createShininess(16f)), VertexAttributes.Usage.Position
                    | VertexAttributes.Usage.Normal);
    Model wallVertical = modelBuilder.createBox(1, 20, 80,
            new Material(ColorAttribute.createDiffuse(Color.FOREST),
                    ColorAttribute.createSpecular(Color.WHITE),
                    FloatAttribute.createShininess(16f)),
            VertexAttributes.Usage.Position |
                    VertexAttributes.Usage.Normal);
    Model groundModel = modelBuilder.createBox(100, 1, 100,
            new Material(ColorAttribute.createDiffuse(Color.GRAY),
                    ColorAttribute.createSpecular(Color.BLUE),
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
        modelBatch.begin(perspectiveCamera);
        engine.update(delta);
        modelBatch.end();
    }

    private void initPersCamera() {
        perspectiveCamera = new PerspectiveCamera(FOV, Core.VIRTUAL_WIDTH, Core.VIRTUAL_HEIGHT);
        perspectiveCamera.position.set(10f, 10f, 10f);
        perspectiveCamera.lookAt(10f, 10f, 10f);
        perspectiveCamera.near = 1f;
        perspectiveCamera.far = 300f;
        perspectiveCamera.update();
    }

    private void initEnvironment() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,
                0.3f, 0.3f, 0.3f, 1f));

        engine = new Engine();
        engine.addSystem(new RenderSystem(modelBatch, environment));
        entity = new Entity();
        entity.add(new ModelComponent(box,10,10,10));
        engine.addEntity(entity);
    }

    private void initModelBatch() {
        modelBatch = new ModelBatch();
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
    }

    /*With the camera set we can now fill in the resize function as well*/
    public void resize(int width, int height) {
        perspectiveCamera.viewportHeight = height;
        perspectiveCamera.viewportWidth = width;
    }

}
