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


import java.text.DecimalFormat; // для вывода коэффицента

public class WinListScreen extends ScreenAdapter {
    Main main;
    ButtonView reverseButton;
    StaticGameObject resultImage;
    BitmapFont font;
    private float scrollY = 0;
    private boolean touch = false; // держим ли мы еще палец
    private float lastY;
    LinkedList<DysplayMatch> dysplay;
    public WinListScreen (Main main) {
        this.main = main;
        reverseButton = new ButtonView(SCREEN_WIDTH/2 - 600, SCREEN_HEIGHT/2 + 200, 150, 150, "reverseHome.png");
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
            dysplay.add(new DysplayMatch(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 + 150 - 200 * i, now, texture));
        }
        this.scrollY = scrollY; // для скроллинга
    }
    public void render(float delta) {
        main.cam.update();
        main.batch.setProjectionMatrix(main.cam.combined);
        ScreenUtils.clear(Color.CLEAR);
        main.batch.begin();
        reverseButton.draw(main.batch);
        for (DysplayMatch i : dysplay) {
            if (i.result.who_win) {
                main.batch.draw(i.texture, i.x - 163, i.y + scrollY - 30, 370, 250);
            } else {
                main.batch.draw(i.texture, i.x - 150, i.y + scrollY, 300, 200);
            }
            font.draw(main.batch, i.result.playerScore + " - " + i.result.enemyScore, i.x - 50, i.y + 50 + scrollY);
        }
        font.draw(main.batch, "All : " + (main.totalWins + main.totalLoses), SCREEN_WIDTH/2 + 400, SCREEN_HEIGHT/2 + 345);
        font.draw(main.batch, "Wins : " + main.totalWins, SCREEN_WIDTH/2 + 400, SCREEN_HEIGHT/2 + 300);
        font.draw(main.batch, "Loses : " + main.totalLoses, SCREEN_WIDTH/2 + 400, SCREEN_HEIGHT/2 + 255);
        float coeff = (main.totalLoses != 0 ? (float)main.totalWins / main.totalLoses : 0);
        DecimalFormat df = new DecimalFormat("0.##");
        String coedS = df.format(coeff);
        font.draw(main.batch, "Coefficient : " + coedS, SCREEN_WIDTH/2 + 400, SCREEN_HEIGHT/2 + 210);

        handleInput();
        main.batch.end();
    }
    private void handleInput() {
        if (Gdx.input.isTouched()) { // касиение хоть когда
            float newscrollY = scrollY - Gdx.input.getDeltaY();
            if (SCREEN_HEIGHT / 2 + 150 + newscrollY >= SCREEN_HEIGHT / 2 + 150 && SCREEN_HEIGHT / 2 + 150 - 200 * dysplay.size() + newscrollY < 100) {
                scrollY = newscrollY;
            }
        }
        if (Gdx.input.justTouched()) { // только новое касание
            main.touch = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (reverseButton.isHit(main.touch.x, main.touch.y)) {
                scrollY = 0;
                main.setScreen(main.menuScreen);
            }
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
