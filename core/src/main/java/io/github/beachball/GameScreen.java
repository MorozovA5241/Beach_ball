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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.beachball.components.ButtonView;

public class GameScreen extends ScreenAdapter {
    Main main;
    GameObject gameObject;
    StaticGameObject box;
    ButtonView rightButton;
    ButtonView leftButton;
    ButtonView jumpButton;

    public GameScreen(Main main) {
        this.main = main;



        gameObject = new GameObject(SCREEN_WIDTH / 2, 800, 200, 300 , main.world, OBJECT_IMG_PATH); // делаем динамический объект
        box = new StaticGameObject(SCREEN_WIDTH / 2, 200, OBJECT_WIDTH, OBJECT_HEIGHT , main.world, OBJECT_IMG_PATH);// делаем статический объект
        rightButton = new ButtonView(100, 100, 80, 120, OBJECT_IMG_PATH);
        jumpButton = new ButtonView(600, 100, 80, 120, OBJECT_IMG_PATH);
        leftButton = new ButtonView(500, 100, 80, 120, OBJECT_IMG_PATH);

    }

    @Override
    public void render(float delta) {
        main.stepWorld();

        main.cam.update();
        main.batch.setProjectionMatrix(main.cam.combined);
        ScreenUtils.clear(Color.CLEAR);


        main.batch.begin();
        gameObject.draw(main.batch);
        rightButton.draw(main.batch);
        leftButton.draw(main.batch);
        jumpButton.draw(main.batch);
        box.draw(main.batch);
        main.batch.end(); // рендер(прорисовка кадра)

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            main.touch = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (rightButton.isHit(main.touch.x, main.touch.y)) {
                gameObject.move(-10);
            }

            else if (leftButton.isHit(main.touch.x, main.touch.y)) {
                gameObject.move(10);
            }
            else if (jumpButton.isHit(main.touch.x, main.touch.y)){
                gameObject.jump();
            }

        }
        else{
            gameObject.move(0);
        }

    }

}
