package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.LostLegacy;
import com.mygdx.game.screens.PlayScreen;

public class Skeleton extends Sprite {

    public enum State {FALLING, JUMPING, STANDING, WALKING, ATTACKING}
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion playerIdle;
    private Animation<TextureRegion> skeletonWalk;
    private Animation<TextureRegion> skeletonJump;
    private Animation<TextureRegion> skeletonAttack;
    public float stateTimer;
    private boolean walkingRigth;
    public boolean attacking;

    public Skeleton(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("idle"));
        this.world = world;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        walkingRigth = true;
        attacking = false;

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 4; i++) {
            frames.add(screen.getAtlas().findRegions("walk").get(i));
        }
        skeletonWalk = new Animation<TextureRegion>(0.25f, frames);
        frames.clear();

        frames.add(screen.getAtlas().findRegions("attack").get(0));
        skeletonAttack = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        frames.add(screen.getAtlas().findRegions("jump").get(0));
        skeletonJump = new Animation<TextureRegion>(0.1f, frames);


        defineSkeleton();
        playerIdle = new TextureRegion(getTexture(), 96, 0, 32, 32);
        setBounds(0 ,0, 32 / LostLegacy.PPM, 32 / LostLegacy.PPM);
        setRegion(playerIdle);
    }

    public void defineSkeleton() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(64 / LostLegacy.PPM , 64 / LostLegacy.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / LostLegacy.PPM);

        fdef.shape = shape;
        fdef.friction = 0.5f; //TODO: alterar

        b2body.createFixture(fdef);
    }

    public void update(float deltaTime) {
        setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
        setRegion(getFrame(deltaTime));
    }

    public TextureRegion getFrame(float deltaTime) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = skeletonJump.getKeyFrame(stateTimer);
                break;
            case WALKING:
                region = skeletonWalk.getKeyFrame(stateTimer, true);
                break;
            case ATTACKING:
                region = skeletonAttack.getKeyFrame(stateTimer);
                break;
            case FALLING:
            case STANDING:
            default:
                region = playerIdle;
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !walkingRigth) && !region.isFlipX()){
            region.flip(true, false);
            walkingRigth = false;
        } else if ((b2body.getLinearVelocity().x > 0 || walkingRigth) && region.isFlipX()) {
            region.flip(true, false);
            walkingRigth = true;
        }

        stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {

        if (attacking)
            return State.ATTACKING;
        else if (b2body.getLinearVelocity().y > 0 || b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0)
            return State.WALKING;
        else
            return State.STANDING;

    }

}
