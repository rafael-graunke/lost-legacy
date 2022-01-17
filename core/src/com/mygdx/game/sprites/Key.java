package com.mygdx.game.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.screens.PlayScreen;

public class Key extends InteractiveTileObject{

    private PlayScreen screen;
    private boolean interacted;

    public Key(World world, TiledMap map, Rectangle bounds, PlayScreen screen) {
        super(world, map, bounds);
        fixture.setUserData(this);
        this.screen = screen;
        interacted = false;
    }

    @Override
    public void onSwordHit() {

    }

    @Override
    public void onBodyHit() {
        Skeleton player = screen.getPlayer();
        if (!interacted)
            player.addKey();
            interacted = true;
    }
}
