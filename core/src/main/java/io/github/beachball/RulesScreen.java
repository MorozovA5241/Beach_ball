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
public class RulesScreen extends ScreenAdapter {
    Main main;
    BitmapFont font;
    ButtonView reverseButton;
    public RulesScreen(Main main) {
        this.main = main;
        reverseButton = new ButtonView(SCREEN_WIDTH/2 - 600, SCREEN_HEIGHT/2 + 200, 150, 150, "reverseHome.png");
        font = new BitmapFont();
        font.getData().setScale(2);
    }

    public void render(float delta) {
        main.cam.update();
        main.batch.setProjectionMatrix(main.cam.combined);
        ScreenUtils.clear(Color.CLEAR);
        main.batch.begin();
        reverseButton.draw(main.batch);
        Texture texture = new Texture("RulesText.png");
        main.batch.draw(texture, SCREEN_WIDTH / 2 - 300, SCREEN_HEIGHT / 2 - 350, 700,700);
        main.batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) { // только новое касание
            main.touch =
                main.cam.unproject(new Vector3(Gdx.input.getX(),
                Gdx.input.getY(), 0));
            if (reverseButton.isHit(main.touch.x, main.touch.y)) {
                main.setScreen(main.menuScreen);
            }
        }
    }

    @Override
    public void dispose() {
        font.dispose();
        reverseButton.dispose();
    }
}
