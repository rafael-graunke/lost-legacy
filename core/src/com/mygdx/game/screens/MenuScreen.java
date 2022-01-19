package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.LostLegacy;

public class MenuScreen implements Screen{
	
	private Viewport viewport;
	private Stage stage;
	private SpriteBatch batch;
	private OrthographicCamera camera;

	private Game game;
	
	public MenuScreen(Game game, SpriteBatch batch) {
		this.batch = batch;
		this.game = game;
		camera = new OrthographicCamera(400, 200);
		viewport = new FitViewport(400, 200, camera);
		camera.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	
	public void update(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			game.setScreen(new PlayScreen((LostLegacy) game));
			dispose();
		}
		camera.update();
	}

	@Override
	public void render(float delta) {		
		update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Texture texture = new Texture(Gdx.files.internal("screen/menu.png"));
		
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(texture, 0, 0);
		batch.end();	
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);		
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
