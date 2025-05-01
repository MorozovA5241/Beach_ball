package io.github.beachball;

import static io.github.beachball.GameSettings.BAFFLE_BIT;
import static io.github.beachball.GameSettings.BALL_BIT;
import static io.github.beachball.GameSettings.BALL_IMAGE_PATH;
import static io.github.beachball.GameSettings.BUTTON_IMG_PATH;
import static io.github.beachball.GameSettings.FLOOR_BIT;
import static io.github.beachball.GameSettings.LEFTBUTTON_IMAGE_PATH;
import static io.github.beachball.GameSettings.OBJECT_HEIGHT;
import static io.github.beachball.GameSettings.OBJECT_IMG_PATH;
import static io.github.beachball.GameSettings.OBJECT_WIDTH;
import static io.github.beachball.GameSettings.PLAYER_BIT;
import static io.github.beachball.GameSettings.PLAYER_IMAGE_PATH;
import static io.github.beachball.GameSettings.RIGHTBUTTON_IMAGE_PATH;
import static io.github.beachball.GameSettings.SCALE;
import static io.github.beachball.GameSettings.SCREEN_HEIGHT;
import static io.github.beachball.GameSettings.SCREEN_WIDTH;
import static io.github.beachball.GameSettings.OBJECT_IMG_PATH;
import static io.github.beachball.GameSettings.SIMPLE_BIT;
import static io.github.beachball.GameSettings.UPBUTTON_IMAGE_PATH;

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

import com.badlogic.gdx.graphics.g2d.BitmapFont; // что бы шерифт был

public class GameScreen extends ScreenAdapter {
    Main main;
    GameObject gameObject;
    StaticGameObject box;
    StaticGameObject wall;
    StaticGameObject rightSideWall;
    StaticGameObject leftSideWall;
    StaticGameObject topSideWall;
    StaticGameObject baffle;
    GameObject ball;
    ButtonView rightButton;
    ButtonView leftButton;
    ButtonView jumpButton;
    ContactManager contactManager;
    boolean check = false; // Что-бы играть до разнице в очках
    int playerScore = 0;
    int enemyScore = 0;
    final int WIN_SCORE = 11;
    BitmapFont font;
    public GameScreen(Main main) {
        this.main = main;

        contactManager = new ContactManager(main.world);

        gameObject = new GameObject(SCREEN_WIDTH / 2 - 200, 200, 200, 200 , main.world, "redPlayer.png", PLAYER_BIT, 1f, 1f, 0f); // делаем динамический объект
        box = new StaticGameObject(SCREEN_WIDTH, 10, OBJECT_WIDTH+ 100000, OBJECT_HEIGHT - 80, main.world, OBJECT_IMG_PATH, FLOOR_BIT);// делаем статический объект
        rightButton = new ButtonView(20, 100, 100, 100, LEFTBUTTON_IMAGE_PATH);
        jumpButton = new ButtonView(1100, 100, 100, 100, UPBUTTON_IMAGE_PATH);
        leftButton = new ButtonView(140, 100, 100, 100, RIGHTBUTTON_IMAGE_PATH);
        wall = new StaticGameObject(SCREEN_WIDTH/2, 30, 20, 680, main.world, "Setka.png", SIMPLE_BIT);
        ball = new GameObject(SCREEN_WIDTH/2 - 100, 600, 100, 100, main.world, "ball.jpg", BALL_BIT, 1.0f, 1.0f, 1.4f);
        //topSideWall = new StaticGameObject(SCREEN_WIDTH / 2, SCREEN_HEIGHT, 3000, 80, main.world, OBJECT_IMG_PATH, SIMPLE_BIT); //под вопросом
        leftSideWall = new StaticGameObject(0, SCREEN_HEIGHT / 2, 3, 5500, main.world, "leftSideWall.png", SIMPLE_BIT);
        rightSideWall = new StaticGameObject(SCREEN_WIDTH, SCREEN_HEIGHT / 2, 3, 5500, main.world, "rightSideWall.png", SIMPLE_BIT);
        baffle = new StaticGameObject(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, 20, 1500, main.world, "Setka.png", BAFFLE_BIT);
        font = new BitmapFont();
        font.getData().setScale(2);
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
        wall.draw(main.batch);
        ball.draw(main.batch);
        font.draw(main.batch, "You: " + playerScore, 50, SCREEN_HEIGHT - 50);
        font.draw(main.batch, "Enemy: " + enemyScore, SCREEN_WIDTH - 250, SCREEN_HEIGHT - 50);
       // topSideWall.draw(main.batch);
        rightSideWall.draw(main.batch);
        leftSideWall.draw(main.batch);
        main.batch.end(); // рендер(прорисовка кадра)
        ball.applyForce(80); // чтобы мячик был легче

        if (ball.needSetPosition == true) {
            if (ball.getX() < SCREEN_WIDTH / 2) {
                enemyScore++;
            } else {
                playerScore++;
            }
            if (enemyScore == playerScore && enemyScore == 10) {
                check = true;
            }
            checkWin(check);
            main.world.destroyBody(ball.body);
            ball = new GameObject(SCREEN_WIDTH/2 - 100, 500, 100, 100, main.world, BALL_IMAGE_PATH, BALL_BIT, 1.0f, 1.0f, 1.4f);

        }
        handleMovementInput();
        handleJumpInput();
    }

    private void handleMovementInput() {
        if (Gdx.input.isTouched()) {
            main.touch = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (rightButton.isHit(main.touch.x, main.touch.y)) {
                gameObject.move(-20);
            }

            else if (leftButton.isHit(main.touch.x, main.touch.y)) {
                gameObject.move(25);
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
    private void checkWin(boolean flag) {
        if (flag) {
            if (playerScore - enemyScore >= 2) {
                playerScore = 0;
                enemyScore = 0;
                main.setScreen(new ResultScreen(main, true));
            } else {
                if (enemyScore - playerScore >= 2) {
                    playerScore = 0;
                    enemyScore = 0;
                    main.setScreen(new ResultScreen(main, false));
                }
            }
        } else {
            if (playerScore >= WIN_SCORE) {
                playerScore = 0;
                enemyScore = 0;
                main.setScreen(new ResultScreen(main, true));
            } else if (enemyScore >= WIN_SCORE) {
                playerScore = 0;
                enemyScore = 0;
                main.setScreen(new ResultScreen(main, false));
            }
        }
    }
}
