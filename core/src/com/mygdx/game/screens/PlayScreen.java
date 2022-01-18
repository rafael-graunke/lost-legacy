package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.LostLegacy;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.sprites.Skeleton;
import com.mygdx.game.tools.B2WorldCreator;
import com.mygdx.game.tools.WorldContactListener;

public class PlayScreen implements Screen{

	private LostLegacy game;
	private TextureAtlas atlas;

	private OrthographicCamera gameCam;
	private Viewport gamePort;
	private Hud hud;
	private Skeleton player;

	//TileMap
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private int mapWidth;
	private int mapHeight;
	private OrthogonalTiledMapRenderer renderer;

	//Box2D
	private World world;
	private Box2DDebugRenderer b2dr;

	private boolean lookingUp;
	private boolean lookingDown;

	public PlayScreen(LostLegacy game) {
		this.game = game;

		atlas = new TextureAtlas("spriteSheets/player.atlas");

		gameCam = new OrthographicCamera();
		gameCam.zoom = 0.3f;
		gamePort = new FitViewport(LostLegacy.V_WIDTH / LostLegacy.PPM , LostLegacy.V_HEIGHT / LostLegacy.PPM, gameCam);		

		mapLoader = new TmxMapLoader();
		map = mapLoader.load("map.tmx");
		mapWidth = map.getProperties().get("width", Integer.class);
		mapHeight = map.getProperties().get("height", Integer.class);
		
		renderer = new OrthogonalTiledMapRenderer(map, 1 / LostLegacy.PPM);

		gameCam.position.set(gamePort.getWorldWidth()/2 * gameCam.zoom, gamePort.getWorldHeight()/2 * gameCam.zoom, 0);

		world = new World(new Vector2(0, -10), true);
		b2dr = new Box2DDebugRenderer();

		new B2WorldCreator(world, map, this);

		player = new Skeleton(world, this);
		
		hud = new Hud(game.batch, player);

		world.setContactListener(new WorldContactListener());
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}
 
	public void handleInput(float deltaTime) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.Z) && player.b2body.getLinearVelocity().y != 0)
			player.attack();
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && (player.getState() == Skeleton.State.STANDING || player.getState() == Skeleton.State.WALKING))
			player.jump();
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
			player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
			player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
		lookingUp = Gdx.input.isKeyPressed(Input.Keys.UP);
		lookingDown = Gdx.input.isKeyPressed(Input.Keys.DOWN);
	}

	public void update(float deltaTime) {
		handleInput(deltaTime);

		world.step(1/60f, 6, 2);

		player.update(deltaTime);

		hud.update(deltaTime);

		gameCam.position.x = player.b2body.getPosition().x;
		
		if (!lookingDown && !lookingUp)
			gameCam.position.y = player.b2body.getPosition().y;
		else if (lookingDown) {
			lookDown();
		}
		else if (lookingUp) {
			lookUp();
		}
		
		if (gameCam.position.y - gamePort.getWorldHeight() / 2 * gameCam.zoom <= 0 )
			gameCam.position.y = 0 + gamePort.getWorldHeight() / 2 * gameCam.zoom;
		if (gameCam.position.x - gamePort.getWorldWidth() / 2 * gameCam.zoom <= 0 )
			gameCam.position.x = 0 + gamePort.getWorldWidth() / 2 * gameCam.zoom;
		if (gameCam.position.y + gamePort.getWorldHeight() / 2 >= map.getProperties().get("height", Integer.class))
			gameCam.position.y = gamePort.getWorldHeight() - gamePort.getWorldHeight() / 2 * gameCam.zoom;
		if (gameCam.position.x + mapWidth / 2 * gameCam.zoom >= mapWidth * gameCam.zoom)
			gameCam.position.x = mapWidth * gameCam.zoom - mapWidth / 2 * gameCam.zoom;
		
		gameCam.update();
		renderer.setView(gameCam);
	}
	
	public void lookUp() {
		float delay = 0.25f;
	    Timer.schedule(new Timer.Task(){
	        @Override
	        public void run() {
	        	if(lookingUp)
	        		gameCam.position.y = player.b2body.getPosition().y + 64 / LostLegacy.PPM;
	        }
	    }, delay);
	}
	
	public void lookDown() {
		float delay = 0.25f;
	    Timer.schedule(new Timer.Task(){
	        @Override
	        public void run() {
	        	if(lookingDown)
	        		gameCam.position.y = player.b2body.getPosition().y - 64 / LostLegacy.PPM;
	        }
	    }, delay);
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.render();

		b2dr.render(world, gameCam.combined);

		game.batch.setProjectionMatrix(gameCam.combined);
		game.batch.begin();
		player.draw(game.batch);
		game.batch.end();

		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		world.dispose();
		b2dr.dispose();
		hud.dispose();
	}

	public Skeleton getPlayer() {
		return player;
	}
}
