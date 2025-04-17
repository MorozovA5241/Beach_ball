package io.github.beachball;

import static io.github.beachball.GameSettings.BUTTON_IMG_PATH;
import static io.github.beachball.GameSettings.OBJECT_IMG_PATH;
import static io.github.beachball.GameSettings.SCREEN_HEIGHT;
import static io.github.beachball.GameSettings.SCREEN_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.beachball.components.ButtonView;

public class MenuScreen extends ScreenAdapter {
    Main main;
    ButtonView  startButton;

    public MenuScreen(Main main) {
        this.main = main;

        startButton = new ButtonView(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, 100, 100, BUTTON_IMG_PATH);

    }

    public void render(float delta){

        main.cam.update();
        main.batch.setProjectionMatrix(main.cam.combined);
        ScreenUtils.clear(Color.CLEAR);
        main.batch.begin();
        startButton.draw(main.batch);
        main.batch.end();

        handleInput();
    }
    private void handleInput(){
        if (Gdx.input.justTouched()) {
            main.touch = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(startButton.isHit(main.touch.x, main.touch.y))
                main.setScreen(main.gameScreen);
        }
    }
}
