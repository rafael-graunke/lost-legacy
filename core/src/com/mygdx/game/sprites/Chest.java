package com.mygdx.game.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.screens.PlayScreen;

public class Chest extends InteractiveTileObject{

    private PlayScreen screen;
    private boolean interacted;

    public Chest(World world, TiledMap map, Rectangle bounds, PlayScreen screen) {
        super(world, map, bounds);
        fixture.setUserData(this);
        this.screen = screen;
    }

    @Override
    public void onSwordHit() {

    }

    @Override
    public void onBodyHit() {
        Skeleton player = screen.getPlayer();
        if (player.hasKey() && !interacted) {
            player.addBone();
            player.removeKey();
            interacted = true;
        }
    }
}
