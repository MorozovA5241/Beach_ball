package io.github.beachball;

import static io.github.beachball.GameSettings.BUTTON_IMG_PATH;
import static io.github.beachball.GameSettings.OBJECT_IMG_PATH;
import static io.github.beachball.GameSettings.SCREEN_HEIGHT;
import static io.github.beachball.GameSettings.SCREEN_WIDTH;
import static io.github.beachball.GameSettings.SIMPLE_BIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.beachball.components.ButtonView;

import com.badlogic.gdx.graphics.g2d.BitmapFont; // что бы шерифт был

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;

import java.util.LinkedList; // подключаем очередь


public class ResultScreen extends ScreenAdapter {
    Main main;
    boolean Win;
    ButtonView  homeButton;

    public ResultScreen(Main main, boolean Win) {
        this.main = main;
        this.Win = Win;
        homeButton = new ButtonView(SCREEN_WIDTH/2 - 100, SCREEN_HEIGHT/2 - 300, 200, 200, "Home.png");
    }

    public void render(float delta){
        main.cam.update();
        main.batch.setProjectionMatrix(main.cam.combined);
        ScreenUtils.clear(Color.CLEAR);
        main.batch.begin();
        Texture texture;
        if (Win == true) {
            texture = new Texture("Win.png");
            main.batch.draw(texture, SCREEN_WIDTH/2 - 300, SCREEN_HEIGHT/2, 500, 400);
        } else {
            texture = new Texture("Lose.png");
            main.batch.draw(texture, SCREEN_WIDTH/2 - 200, SCREEN_HEIGHT/2, 400, 300);
        }

        homeButton.draw(main.batch);
        main.batch.end();

        handleInput();
    }
    private void handleInput(){
        if (Gdx.input.justTouched()) {
            main.touch = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(homeButton.isHit(main.touch.x, main.touch.y))
                main.setScreen(main.menuScreen);
        }
    }
}
