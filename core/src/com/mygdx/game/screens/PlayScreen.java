package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.LostLegacy;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.sprites.Skeleton;
import com.sun.prism.image.ViewPort;

public class PlayScreen implements Screen{

	private LostLegacy game;
	private OrthographicCamera gameCam;
	private Viewport gamePort;
	private Hud hud;
	private Skeleton player;

	//TileMap
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	//Box2D
	private World world;
	private Box2DDebugRenderer b2dr;

	public PlayScreen(LostLegacy game) {
		this.game = game;

		gameCam = new OrthographicCamera();
		gameCam.zoom = 0.4f;
		gamePort = new FitViewport(LostLegacy.V_WIDTH / LostLegacy.PPM , LostLegacy.V_HEIGHT / LostLegacy.PPM, gameCam);

		hud = new Hud(game.batch);

		mapLoader = new TmxMapLoader();
		map = mapLoader.load("map.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / LostLegacy.PPM);

		gameCam.position.set(gamePort.getWorldWidth()/2 * gameCam.zoom, gamePort.getWorldHeight()/2 * gameCam.zoom, 0);

		world = new World(new Vector2(0, -10), true);
		b2dr = new Box2DDebugRenderer();

		//Temporario
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;
		//Chao
		for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth()/2) / LostLegacy.PPM, (rect.getY() + rect.getHeight()/2) / LostLegacy.PPM);

			body  = world.createBody(bdef);

			shape.setAsBox(rect.getWidth()/2 / LostLegacy.PPM , rect.getHeight()/2 / LostLegacy.PPM );
			fdef.shape = shape;
			body.createFixture(fdef);
		}
		//baus
		for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth()/2) / LostLegacy.PPM, (rect.getY() + rect.getHeight()/2) / LostLegacy.PPM);

			body  = world.createBody(bdef);

			shape.setAsBox(rect.getWidth()/2 / LostLegacy.PPM , rect.getHeight()/2 / LostLegacy.PPM );
			fdef.shape = shape;
			body.createFixture(fdef);
		}
		//espinhos
		for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth()/2) / LostLegacy.PPM, (rect.getY() + rect.getHeight()/2) / LostLegacy.PPM);

			body  = world.createBody(bdef);

			shape.setAsBox(rect.getWidth()/2 / LostLegacy.PPM , rect.getHeight()/2 / LostLegacy.PPM );
			fdef.shape = shape;
			body.createFixture(fdef);
		}
		player = new Skeleton(world);

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	public void handleInput(float deltaTime) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
			player.b2body.applyLinearImpulse(new Vector2(0, 4), player.b2body.getWorldCenter(), true);
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
			player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
			player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

	}

	public void update(float deltaTime) {
		handleInput(deltaTime);

		world.step(1/60f, 6, 2);


		gameCam.position.x = player.b2body.getPosition().x;
		gameCam.position.y = player.b2body.getPosition().y;
		if (gameCam.position.y - gamePort.getWorldHeight() / 2 * gameCam.zoom <= 0 )
			gameCam.position.y = 0 + gamePort.getWorldHeight() / 2 * gameCam.zoom;
		if (gameCam.position.x - gamePort.getWorldWidth() / 2 * gameCam.zoom <= 0 )
			gameCam.position.x = 0 + gamePort.getWorldWidth() / 2 * gameCam.zoom;
		if (gameCam.position.y + gamePort.getWorldHeight() / 2 * gameCam.zoom >= gamePort.getWorldHeight() )
			gameCam.position.y = gamePort.getWorldHeight() - gamePort.getWorldHeight() / 2 * gameCam.zoom;
		if (gameCam.position.x + gamePort.getWorldWidth() / 2 * gameCam.zoom >= gamePort.getWorldWidth() )
			gameCam.position.x = gamePort.getWorldWidth() - gamePort.getWorldWidth() / 2 * gameCam.zoom;

		gameCam.update();
		renderer.setView(gameCam);
	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.render();

		b2dr.render(world, gameCam.combined);

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
		// TODO Auto-generated method stub
		
	}

}
