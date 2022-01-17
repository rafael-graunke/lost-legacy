package com.mygdx.game.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.LostLegacy;
import com.mygdx.game.screens.PlayScreen;

public class Checkpoint extends InteractiveTileObject{

    private PlayScreen screen;

    public Checkpoint(World world, TiledMap map, Rectangle bounds, PlayScreen screen) {
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
        player.setCheckpoint(new Vector2(bounds.getX() / LostLegacy.PPM, bounds.getY() / LostLegacy.PPM));
    }
}
