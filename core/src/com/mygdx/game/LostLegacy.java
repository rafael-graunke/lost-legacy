package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.screens.MenuScreen;
import com.mygdx.game.screens.PlayScreen;

public class LostLegacy extends Game {
	
	public SpriteBatch batch;
	public static final int V_WIDTH = 1600;
	public static final int V_HEIGHT = 800;
	public static final float PPM = 100;
	
	public static final short DEFAULT_BIT = 1;
	public static final short SKELETON_BIT = 2;
	public static final short KEY_BIT = 4;
	public static final short CHEST_BIT = 8;
	public static final short OPEN_BIT = 16;
	public static final short COLLECTED_BIT = 32;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MenuScreen(this, batch));
	}

	@Override
	public void render () {
		super.render();
		
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
