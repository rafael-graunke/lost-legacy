package com.mygdx.game.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Chest extends InteractiveTileObject{

    public Chest(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
    }

}
