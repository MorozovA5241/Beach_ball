package io.github.beachball;

import static io.github.beachball.GameSettings.BUTTON_IMG_PATH;
import static io.github.beachball.GameSettings.FLOOR_BIT;
import static io.github.beachball.GameSettings.OBJECT_HEIGHT;
import static io.github.beachball.GameSettings.OBJECT_IMG_PATH;
import static io.github.beachball.GameSettings.OBJECT_WIDTH;
import static io.github.beachball.GameSettings.PLAYER_BIT;
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
import io.github.beachball.managers.ContactManager;

public class GameScreen extends ScreenAdapter {
    Main main;
    GameObject gameObject;
    StaticGameObject box;
    ButtonView rightButton;
    ButtonView leftButton;
    ButtonView jumpButton;
    ContactManager contactManager;

    public GameScreen(Main main) {
        this.main = main;

        contactManager = new ContactManager(main.world);

        gameObject = new GameObject(SCREEN_WIDTH / 2, 800, 200, 300 , main.world, OBJECT_IMG_PATH, PLAYER_BIT); // делаем динамический объект
        box = new StaticGameObject(SCREEN_WIDTH / 2, 20, OBJECT_WIDTH, OBJECT_HEIGHT , main.world, OBJECT_IMG_PATH, FLOOR_BIT);// делаем статический объект
        rightButton = new ButtonView(20, 100, 80, 220, OBJECT_IMG_PATH);
        jumpButton = new ButtonView(600, 100, 80, 220, BUTTON_IMG_PATH);
        leftButton = new ButtonView(120, 100, 80, 220, OBJECT_IMG_PATH);

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

        handleMovementInput();
        handleJumpInput();
    }

    private void handleMovementInput() {
        if (Gdx.input.isTouched()) {
            main.touch = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (rightButton.isHit(main.touch.x, main.touch.y)) {
                gameObject.move(-10);
            }

            else if (leftButton.isHit(main.touch.x, main.touch.y)) {
                gameObject.move(10);
            }
            else
                gameObject.move(0);

        }
        else
            gameObject.move(0);



    }
    private void handleJumpInput(){
        if (Gdx.input.justTouched()) {
            main.touch = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(jumpButton.isHit(main.touch.x, main.touch.y))
                gameObject.jump();
        }
    }

}
