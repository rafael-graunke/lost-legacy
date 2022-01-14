package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.LostLegacy;

public class Skeleton extends Sprite {

    public World world;
    public Body b2body;

    public Skeleton(World world) {
        this.world = world;
        defineSkeleton();
    }

    public void defineSkeleton() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / LostLegacy.PPM , 128 / LostLegacy.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / LostLegacy.PPM);

        fdef.shape = shape;

        b2body.createFixture(fdef);
    }

}
