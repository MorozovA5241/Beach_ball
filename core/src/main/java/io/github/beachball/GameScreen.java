package io.github.beachball;

import static io.github.beachball.GameSettings.OBJECT_IMG_PATH;
import static io.github.beachball.GameSettings.SCREEN_WIDTH;
import static io.github.beachball.GameSettings.OBJECT_IMG_PATH;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends ScreenAdapter {
    Main main;
    GameObject gameObject;
    public GameScreen(Main main) {
        this.main = main;

        gameObject = new GameObject(SCREEN_WIDTH / 2, 500, 100, 300, main.world, OBJECT_IMG_PATH);

    }

    @Override
    public void render(float delta) {
        main.stepWorld();

        main.cam.update();
        main.batch.setProjectionMatrix(main.cam.combined);
        ScreenUtils.clear(Color.CLEAR);

        main.batch.begin();
        gameObject.draw(main.batch);
        main.batch.end();
    }

}
