package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.LostLegacy;
import com.mygdx.game.screens.PlayScreen;

public class Spike extends InteractiveTileObject {

    private PlayScreen screen;

    public Spike(World world, TiledMap map, Rectangle bounds, PlayScreen screen) {
        super(world, map, bounds);
        fixture.setUserData(this);
        this.screen = screen;
    }

    @Override
    public void onSwordHit() {
        Gdx.app.log("Spike", "Collision");
        Skeleton player = screen.getPlayer();
        if (player.currentState == Skeleton.State.ATTACKING)
            player.jump();

    }

}
