package io.github.beachball;

import static io.github.beachball.GameSettings.SCREEN_HEIGHT;
import static io.github.beachball.GameSettings.SCREEN_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.beachball.components.ButtonView;

public class ModeScreen extends ScreenAdapter {
    Main main;
    ButtonView modeOneButton;
    ButtonView modeTwoButton;

    public ModeScreen(Main main) {
        this.main = main;
        modeOneButton = new ButtonView(SCREEN_WIDTH/2 - 500, SCREEN_HEIGHT/2 - 100, 250, 250, "modeOne.png");
        modeTwoButton = new ButtonView(SCREEN_WIDTH/2 + 200, SCREEN_HEIGHT/2 - 135, 300, 300, "modeTwo.png");
    }

    public void render(float delta) {
        main.cam.update();
        main.batch.setProjectionMatrix(main.cam.combined);
        ScreenUtils.clear(Color.CLEAR);
        main.batch.begin();
        modeOneButton.draw(main.batch);
        modeTwoButton.draw(main.batch);
        main.batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) { // только новое касание
            main.touch = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (modeOneButton.isHit(main.touch.x, main.touch.y)) {
                main.mode = true;
                main.setScreen(main.menuScreen);
            }
            if (modeTwoButton.isHit(main.touch.x, main.touch.y)) {
                main.mode = false;
                main.setScreen(main.menuScreen);
            }
        }
    }

    @Override
    public void dispose() {
        modeOneButton.dispose();
        modeTwoButton.dispose();
    }
}
