package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.LostLegacy;
import com.mygdx.game.screens.PlayScreen;

public class Skeleton extends Sprite {

    public enum State {FALLING, JUMPING, STANDING, WALKING, ATTACKING}
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private Vector2 checkpoint;
    private TextureRegion playerIdle;
    private Animation<TextureRegion> skeletonWalk;
    private Animation<TextureRegion> skeletonJump;
    private Animation<TextureRegion> skeletonAttack;
    public float stateTimer;
    private boolean walkingRigth;
    public boolean attacking;
    private int health;
    private int bones;
    private int keys;
    private PlayScreen screen;

    public Skeleton(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("idle"));
        this.screen = screen;
        this.world = world;

        health = 3;
        bones = 0;
        keys = 0;
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
        fdef.friction = 0.5f;
        fdef.filter.categoryBits = LostLegacy.SKELETON_BIT;
        fdef.filter.maskBits = LostLegacy.DEFAULT_BIT | LostLegacy.CHEST_BIT | LostLegacy.KEY_BIT;
        

        PolygonShape body = new PolygonShape();
        body.set(new Vector2[] {
                new Vector2(-7 / LostLegacy.PPM, 4/ LostLegacy.PPM),
                new Vector2(-4 / LostLegacy.PPM, 7/ LostLegacy.PPM),
                new Vector2(4 / LostLegacy.PPM, 7/ LostLegacy.PPM),
                new Vector2(7 / LostLegacy.PPM, 4/ LostLegacy.PPM),
                new Vector2(7 / LostLegacy.PPM, -6/ LostLegacy.PPM),
                new Vector2(-7 / LostLegacy.PPM, -6/ LostLegacy.PPM)
        });

        fdef.shape = body;
        b2body.createFixture(fdef).setUserData("body");

//        EdgeShape sword = new EdgeShape();
//        sword.set(new Vector2(-6 / LostLegacy.PPM, -8 / LostLegacy.PPM), new Vector2(6 / LostLegacy.PPM, -8 / LostLegacy.PPM));
        PolygonShape sword = new PolygonShape();
        sword.set(new Vector2[] {
                new Vector2(-6 / LostLegacy.PPM, 0),
                new Vector2(6 / LostLegacy.PPM, 0),
                new Vector2(-6 / LostLegacy.PPM, -12 / LostLegacy.PPM),
                new Vector2(6 / LostLegacy.PPM, -12 / LostLegacy.PPM)
        });
        fdef.shape = sword;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("sword");
    }

    public void update(float deltaTime) {
        if (b2body.getLinearVelocity().y == 0) {
            attacking = false;
        }
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight()/2);
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

    //Movimento
    public void jump() {
        b2body.applyLinearImpulse(new Vector2(0, 3.5f - b2body.getLinearVelocity().y), b2body.getWorldCenter(), true);
    }

    public void hit() {
        float delay = 0.01f;
        if (health > 0) {
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    b2body.setLinearVelocity(0,0);
                    b2body.setTransform(checkpoint, b2body.getAngle());
                    health --;
                }
            }, delay);
        } else {
            die();
        }
    }

    public void die() {
        screen.die();
    }

    public void win() {
        System.out.println("Ganhou!"); //TODO: Alterar
    }

    public void attack() {
        float delay = 0.25f;
        attacking = true;
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                attacking = false;
            }
        }, delay);
    }

    public void addBone() {
        if (bones < 6) {
            bones ++;
        } else {
            win();
        }
    }

    public boolean hasKey() {
        return keys != 0;
    }

    public void addKey() {
        keys ++;
    }

    public void removeKey() {
        keys --;
    }

    public void setCheckpoint(Vector2 checkpoint) {
        this.checkpoint = checkpoint;
    }
    
    public int getKeys() {
    	return keys;
    }
    
    public int getBones() {
    	return bones;
    }
    
    public int getHealth() {
    	return health;
    }
}
