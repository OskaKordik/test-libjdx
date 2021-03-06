package com.mygdx.game.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameWorld;
import com.mygdx.game.components.CharacterComponent;
import com.mygdx.game.components.ModelComponent;
import com.mygdx.game.components.PlayerComponent;

import sun.rmi.runtime.Log;

public class PlayerSystem extends EntitySystem implements EntityListener {
    private Entity player;
    private PlayerComponent playerComponent;
    private CharacterComponent characterComponent;
    private ModelComponent modelComponent;
    private final Vector3 vector3 = new Vector3();
    private final Camera camera;
    private GameWorld gameWorld;

    public PlayerSystem(GameWorld gameWorld, Camera camera) {
        this.camera = camera;
        this.gameWorld = gameWorld;
    }

    @Override
    public void addedToEngine(Engine engine) {
        engine.addEntityListener(Family.all
                (PlayerComponent.class).get(), this);
    }

    @Override
    public void entityAdded(Entity entity) {
        player = entity;
        playerComponent =
                entity.getComponent(PlayerComponent.class);
        characterComponent =
                entity.getComponent(CharacterComponent.class);
        modelComponent = entity.getComponent(ModelComponent.class);
    }

    @Override
    public void entityRemoved(Entity entity) {

    }

    @Override
    public void update(float delta) {
        if (player == null) return;
        updateMovement(delta);
    }

    private void updateMovement(float delta) {
        float deltaX = -Gdx.input.getDeltaX() * 0.5f;
        float deltaY = -Gdx.input.getDeltaY() * 0.5f;

        vector3.set(0, 0, 0);
        camera.rotate(camera.up, deltaX);
        vector3.set(camera.direction).crs(camera.up).nor();
        camera.direction.rotate(vector3, deltaY);
        vector3.set(0, 0, 0);
        characterComponent.characterDirection.set(-1, 0, 0).rot(modelComponent.instance.transform).nor();
        characterComponent.walkDirection.set(0, 0, 0);
//        if (Gdx.input.isKeyPressed(Input.Keys.W)) characterComponent.walkDirection.add(camera.direction);
//        if (Gdx.input.isKeyPressed(Input.Keys.S)) characterComponent.walkDirection.sub(camera.direction);
//        if (Gdx.input.isKeyPressed(Input.Keys.A)) tmp.set(camera.direction).crs(camera.up).scl(-1);
//        if (Gdx.input.isKeyPressed(Input.Keys.D)) tmp.set(camera.direction).crs(camera.up);
//        if (Gdx.input.isTouched()) {
//            characterComponent.walkDirection.add(camera.direction);
//            vector3.set(camera.direction).crs(camera.up);
//            System.out.println("Touched! **** ");
//        }
//        if (Gdx.input.isTouched()) vector3.set(camera.direction).crs(camera.up);

        characterComponent.walkDirection.add(vector3);
//        characterComponent.walkDirection.scl(10f * delta);
        characterComponent.characterController.setWalkDirection(characterComponent.walkDirection);
        Matrix4 ghost = new Matrix4();
        Vector3 translation = new Vector3();
        characterComponent.ghostObject.getWorldTransform(ghost); //TODO export
        ghost.getTranslation(translation);
        modelComponent.instance.transform.set(
                0f, translation.y, translation.z,
                0f, camera.direction.y, camera.direction.z, 0);

//        camera.position.set(0f, translation.y, translation.z);
        camera.update(true);
    }
}