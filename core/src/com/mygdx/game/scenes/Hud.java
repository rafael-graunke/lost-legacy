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
import com.mygdx.game.sprites.Skeleton;

public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;

    private int worldTimer;
    private float timeCount;
    private int score;
    
    private Skeleton player;

    Label lifeLabel;
    Label timeLabel;
    Label keysLabel;
    Label bonesLabel;

    public Hud(SpriteBatch sb, Skeleton player) {
        worldTimer = 0;
        timeCount = 0;
        score = 0;
        
        this.player = player;

        viewport = new FitViewport(LostLegacy.V_WIDTH, LostLegacy.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        timeLabel = new Label("Tempo: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        keysLabel = new Label("Chaves: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        bonesLabel = new Label("Ossos: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        lifeLabel = new Label("Vida: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        
        Label fill = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(timeLabel).expandX().padTop(10).colspan(10);
        
        table.row();
        table.add(fill).colspan(7);
        table.add(keysLabel).expandX().colspan(1);
        table.add(bonesLabel).expandX().colspan(1);
        table.add(lifeLabel).expandX().colspan(1);

        stage.addActor(table);
    }

    public void update(float deltaTime) {
        timeCount += deltaTime;
        if (timeCount >= 1) {
            worldTimer ++;
            timeLabel.setText("Tempo: " + String.format("%03d", worldTimer));
            timeCount = 0;
        }
        keysLabel.setText("Chaves: " + player.getKeys());
        bonesLabel.setText("Ossos: " + player.getBones());
        lifeLabel.setText("Vida: " + player.getHealth());
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public int getWorldTimer() {
        return worldTimer;
    }
}
