package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.LostLegacy;

public class WinScreen implements Screen{

	
	private Viewport viewport;
	private Stage stage;
	private SpriteBatch batch;
	private OrthographicCamera camera;

	private Game game;
	
	public WinScreen(Game game, SpriteBatch batch) {
		this.batch = batch;
		this.game = game;
		viewport = new FitViewport(400, 200, new OrthographicCamera());
		stage = new Stage(viewport, ((LostLegacy) game).batch);
		
		Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
		Table table = new Table();
		table.center();
		table.setFillParent(true);
		
		Label timeCount = new Label("Ossos Coletados: " + ((PlayScreen) game.getScreen()).getPlayer().getBones(), font);
		Label bonesCount = new Label("Tempo em segundos: " + ((PlayScreen) game.getScreen()).getHud().getWorldTimer(), font);
		table.add(timeCount).expandX();
		table.row();
		table.add(bonesCount).expandX();

		stage.addActor(table);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	public void update(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			game.setScreen(new PlayScreen((LostLegacy) game));
			dispose();
		}
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Texture texture = new Texture(Gdx.files.internal("screen/win.png"));

		batch.begin();
		batch.draw(texture, 0, 0);
		batch.end();

		stage.draw();		
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
		stage.dispose();		
	}

}
