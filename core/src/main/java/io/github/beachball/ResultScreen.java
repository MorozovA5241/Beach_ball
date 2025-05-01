package io.github.beachball;

import static io.github.beachball.GameSettings.BUTTON_IMG_PATH;
import static io.github.beachball.GameSettings.OBJECT_IMG_PATH;
import static io.github.beachball.GameSettings.SCREEN_HEIGHT;
import static io.github.beachball.GameSettings.SCREEN_WIDTH;

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

public class ResultScreen extends ScreenAdapter {
    Main main;
    boolean Win;
    SpriteBatch batch;
    Texture resultImage;
    ButtonView  homeButton;
    MenuScreen menuScreen;

    public ResultScreen(Main main, boolean Win) {
        this.main = main;
        this.Win = Win;
        menuScreen = new MenuScreen(this.main);
        batch = new SpriteBatch();
        if (Win == true) {
            resultImage = new Texture("Win.png");
        } else {
            resultImage = new Texture("Lose.png");
        }

        homeButton = new ButtonView(SCREEN_WIDTH/2 - 100, SCREEN_HEIGHT/2 - 300, 200, 200, "Home.png");
    }

    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(resultImage,
            (Gdx.graphics.getWidth() - resultImage.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - resultImage.getHeight()) / 2f + 300);
        batch.end();

        main.batch.begin();
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
    @Override
    public void dispose() {
        batch.dispose();
        resultImage.dispose();
    }
}
