package io.github.beachball;

import static io.github.beachball.GameSettings.OBJECT_HEIGHT;
import static io.github.beachball.GameSettings.OBJECT_IMG_PATH;
import static io.github.beachball.GameSettings.OBJECT_WIDTH;
import static io.github.beachball.GameSettings.SCALE;
import static io.github.beachball.GameSettings.SCREEN_HEIGHT;
import static io.github.beachball.GameSettings.SCREEN_WIDTH;
import static io.github.beachball.GameSettings.OBJECT_IMG_PATH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends ScreenAdapter {
    Main main;
    GameObject gameObject;
    StaticGameObject box;
    public GameScreen(Main main) {
        this.main = main;

        //gameObject = new GameObject(SCREEN_WIDTH / 2, 500, OBJECT_WIDTH, OBJECT_HEIGHT , main.world, OBJECT_IMG_PATH);
        gameObject = new GameObject(SCREEN_WIDTH / 2, 800, 200, 300 , main.world, OBJECT_IMG_PATH); // делаем динамический объект
        box = new StaticGameObject(SCREEN_WIDTH / 2, 200, OBJECT_WIDTH, OBJECT_HEIGHT , main.world, OBJECT_IMG_PATH);  // делаем статический объект
    }

    @Override
    public void render(float delta) {
        main.stepWorld();

        main.cam.update();
        main.batch.setProjectionMatrix(main.cam.combined);
        ScreenUtils.clear(Color.CLEAR);

        if (Gdx.input.isTouched()) {
            if(Gdx.input.getX()>SCREEN_WIDTH/2) {
                gameObject.move(10.0f);
            }
            else{
                gameObject.move(-10.0f);
            }
        }



        main.batch.begin();
        gameObject.draw(main.batch);
        main.batch.end();

        main.batch.begin();
        box.draw(main.batch);
        main.batch.end(); // рендер(прорисовка кадра)
    }

}
