package com.mygdx.game.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.LostLegacy;

public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;

    private int worldTimer;
    private float timeCount;
    private int score;

    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;

    public Hud(SpriteBatch sb) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(LostLegacy.V_WIDTH, LostLegacy.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("LEVEL", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();

        stage.addActor(table);
    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
