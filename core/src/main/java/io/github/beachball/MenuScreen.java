package io.github.beachball;

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
    ButtonView exitButton;
    ButtonView listButton;
    ButtonView rulesButton;
    ButtonView modeButtonTwo;
    ButtonView modeButtonOne;

    public MenuScreen(Main main) {
        this.main = main;
        modeButtonOne = new ButtonView(SCREEN_WIDTH / 2 - 450, SCREEN_HEIGHT / 2 + 40, 150, 150, "modeOne.png");
        modeButtonTwo = new ButtonView(SCREEN_WIDTH / 2 - 450, SCREEN_HEIGHT / 2 + 40, 150, 150, "modeTwo.png");
        startButton = new ButtonView(SCREEN_WIDTH/2 - 200, SCREEN_HEIGHT/2 + 20, 400, 300, "Button_Play.png");
        exitButton = new ButtonView(SCREEN_WIDTH/2 - 600, SCREEN_HEIGHT/2 - 250, 200, 100, "Exit.png");
        listButton = new ButtonView(SCREEN_WIDTH/2 + 100, SCREEN_HEIGHT/2 - 300, 200, 300, "ListOfMaches.png");
        rulesButton = new ButtonView(SCREEN_WIDTH/2 - 230, SCREEN_HEIGHT/2 - 300, 250, 250, "Rules.png");
    }

    public void render(float delta){
        main.cam.update();
        main.batch.setProjectionMatrix(main.cam.combined);
        ScreenUtils.clear(Color.CLEAR);
        main.batch.begin();
        if (main.mode) {
            modeButtonOne.draw(main.batch);
        } else {
            modeButtonTwo.draw(main.batch);
        }
        startButton.draw(main.batch);
        exitButton.draw(main.batch);
        listButton.draw(main.batch);
        rulesButton.draw(main.batch);
        main.batch.end();
        handleInput();
    }
    private void handleInput(){
        if (Gdx.input.justTouched()) {
            main.touch = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(startButton.isHit(main.touch.x, main.touch.y)) {
                if (main.mode) {
                    main.gameScreenTwo = new GameScreenTwo(main);
                    main.setScreen(main.gameScreenTwo);
                } else {
                    main.gameScreenOne = new GameScreenOne(main);
                    main.setScreen(main.gameScreenOne);
                }
            }
            if(exitButton.isHit(main.touch.x, main.touch.y)) {
                Gdx.app.exit();
            }
            if(listButton.isHit(main.touch.x, main.touch.y)) {
                main.winlistScreen = new WinListScreen(main);
                main.setScreen(main.winlistScreen);
            }
            if(rulesButton.isHit(main.touch.x, main.touch.y)) {
                main.setScreen(new RulesScreen(main));
            }
            if(modeButtonTwo.isHit(main.touch.x, main.touch.y)) {
                main.setScreen(new ModeScreen(main));
            }
        }
    }
}
