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

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.BitmapFont; // что бы шерифт был
public class WinListScreen extends ScreenAdapter {
    Main main;
    ButtonView reverseButton;
    StaticGameObject resultImage;
    BitmapFont font;

    LinkedList<DysplayMatch> dysplay;
    public WinListScreen (Main main) {
        this.main = main;
        reverseButton = new ButtonView(SCREEN_WIDTH/2 - 500, SCREEN_HEIGHT/2 + 100, 200, 200, "reverseHome.png");
        font = new BitmapFont();
        font.getData().setScale(2);

        dysplay = new LinkedList<>();
        for (int i = 0; i < main.history.size(); i++) {
            MatchResult now = main.history.get(i);
            Texture texture;
            if (now.who_win) {
                texture = new Texture("Win.png");
            } else {
                texture = new Texture("Lose.png");
            }
            dysplay.add(new DysplayMatch(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 + 150 - 100 * i, now, texture));
        }
    }

    public void render(float delta) {
        main.cam.update();
        main.batch.setProjectionMatrix(main.cam.combined);
        ScreenUtils.clear(Color.CLEAR);
        main.batch.begin();
        reverseButton.draw(main.batch);
        for (DysplayMatch i : dysplay) {
            main.batch.draw(i.texture, i.x - 150, i.y, 300, 200);
            font.draw(main.batch, i.result.playerScore + " - " + i.result.enemyScore, i.x - 50, i.y + 50);
        }
        main.batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            main.touch = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(reverseButton.isHit(main.touch.x, main.touch.y))
                main.setScreen(main.menuScreen);
        }
    }

    @Override
    public void dispose() {
        for (DysplayMatch i : dysplay) {
            i.texture.dispose();
        }
        font.dispose();
        reverseButton.dispose();
    }
}
