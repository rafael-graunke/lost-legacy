package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends Sprite {

    public World world;
    public Body b2body;


    final float SPEED = 3f;
    boolean jumping;
    boolean walking;
    Animation<TextureRegion> currentAnimation;
    Animation<TextureRegion> walkingAnimation;
    Animation<TextureRegion> jumpingAnimation;
    Animation<TextureRegion> attackAnimation;
    Animation<TextureRegion> idleAnimation;
    float x;
    float y;

    public Player(TextureAtlas atlas) {

        walkingAnimation = new Animation<TextureRegion>(0.5f/4f, atlas.findRegions("walk"), Animation.PlayMode.LOOP);
        jumpingAnimation = new Animation<TextureRegion>(0.5f/4f, atlas.findRegions("jump"));
        attackAnimation = new Animation<TextureRegion>(0.5f/4f, atlas.findRegions("attack"));
        idleAnimation = new Animation<TextureRegion>(0.5f/4f, atlas.findRegions("idle"), Animation.PlayMode.LOOP);
    }

    public void create () {
        currentAnimation = idleAnimation;
        x = 0;
        y = 0;
    }

    public void update() {
        currentAnimation = idleAnimation;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += SPEED;
            currentAnimation = walkingAnimation;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= SPEED;
            currentAnimation = walkingAnimation;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += SPEED;
            currentAnimation = jumpingAnimation;
        } else if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            currentAnimation = attackAnimation;
        }
    }

    private void jump() {
        b2body.applyForceToCenter(0, 80f, true);
    }

    public void render(SpriteBatch batch, float stateTime) {
        batch.draw(currentAnimation.getKeyFrame(stateTime), x, y);
    }

}
