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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.beachball.components.ButtonView;
import io.github.beachball.managers.ContactManager;

import com.badlogic.gdx.graphics.g2d.BitmapFont; // что бы шерифт был

import java.util.HashSet;

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
    public boolean moved = false;
    HashSet<Integer> activePointers = new HashSet<>(); // для мультитача
    public GameScreen(Main main) {
        this.main = main;

        contactManager = new ContactManager(main.world);

        gameObject = new GameObject(SCREEN_WIDTH / 2 - 200, 200, 200, 200 , main.world, "redPlayer.png", PLAYER_BIT, 1f, 1f, 0f); // делаем динамический объект
        box = new StaticGameObject(SCREEN_WIDTH, 10, OBJECT_WIDTH+ 100000, OBJECT_HEIGHT - 80, main.world, OBJECT_IMG_PATH, FLOOR_BIT);// делаем статический объект
        rightButton = new ButtonView(0, 60, 200, 200, "left.png");
        jumpButton = new ButtonView(1100, 60, 200, 200, "up.png");
        leftButton = new ButtonView(160, 60, 200, 200, "right.png");
        wall = new StaticGameObject(SCREEN_WIDTH/2, 30, 20, 700, main.world, "Setka.png", SIMPLE_BIT);
        ball = new GameObject(SCREEN_WIDTH/2 - 100, 600, 60, 60, main.world, "ball.png", BALL_BIT, 1.0f, 1.0f, 1.4f);
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
        main.batch.setColor(1, 1, 1, 0.7f); // прозрачность
        rightButton.draw(main.batch);
        leftButton.draw(main.batch);
        jumpButton.draw(main.batch);
        main.batch.setColor(1, 1, 1, 1); // прозрачность

        box.draw(main.batch);
        wall.draw(main.batch);
        ball.draw(main.batch);
        font.draw(main.batch, "You: " + playerScore, 50, SCREEN_HEIGHT - 50);
        font.draw(main.batch, "Enemy: " + enemyScore, SCREEN_WIDTH - 250, SCREEN_HEIGHT - 50);
       // topSideWall.draw(main.batch);
        rightSideWall.draw(main.batch);
        leftSideWall.draw(main.batch);
        main.batch.end(); // рендер(прорисовка кадра)
        ball.applyForce(35); // чтобы мячик был легче

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
            ball = new GameObject(SCREEN_WIDTH/2 - 100, 500, 60, 60, main.world, "ball.png", BALL_BIT, 1.0f, 1.0f, 1.4f);

        }
        for (int i = 0; i < 10; i++) {
            handleJumpInput(i);
            handleInput(i);
        }
        if (!moved) {
            gameObject.move(0);
        }
        moved = false;
    }
    private void handleInput(int i) {
        boolean touchMove = false; // Чтобы мяч не прыгал по диагонали
        if (Gdx.input.isTouched(i)) {
            main.touch = main.cam.unproject(new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0));
            if (rightButton.isHit(main.touch.x, main.touch.y)) {
                gameObject.move(-20);
                moved = true;
                touchMove = true;
            }
            if (leftButton.isHit(main.touch.x, main.touch.y)) {
                gameObject.move(25);
                moved = true;
                touchMove = true;
            }
            activePointers.add(i);
        } else {
            activePointers.remove(i);
        }
    }
    private void handleMovementInput(int i) {
        if (Gdx.input.isTouched(i)) {
            main.touch = main.cam.unproject(new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0));

            if (rightButton.isHit(main.touch.x, main.touch.y)) {
                gameObject.move(-20);
            } else {
                if (leftButton.isHit(main.touch.x, main.touch.y)) {
                    gameObject.move(25);
                } else {
                    gameObject.move(0);
                }
            }
        } else {
            gameObject.move(0);
        }
    }
    private void handleJumpInput(int i){
        if (Gdx.input.isTouched(i)) {
            if (!activePointers.contains(i)) {
                main.touch = main.cam.unproject(new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0));
                if (jumpButton.isHit(main.touch.x, main.touch.y)) {
                    gameObject.jump();
                }
                activePointers.add(i);
            }
        } else {
            activePointers.remove(i);
        }
    }
    private void checkWin(boolean flag) {
        if (flag) {
            if (playerScore - enemyScore >= 2) {
                if (main.history.size() >= 20) {
                    main.history.removeFirst();
                }
                main.history.add(new MatchResult(true, playerScore, enemyScore));
                main.totalWins++;
                main.saveHistory();
                playerScore = 0;
                enemyScore = 0;
                check = false;
                main.setScreen(new ResultScreen(main, true));
            } else {
                if (enemyScore - playerScore >= 2) {
                    if (main.history.size() >= 20) {
                        main.history.removeFirst();
                    }
                    main.history.add(new MatchResult(false, playerScore, enemyScore));
                    main.totalLoses++;
                    main.saveHistory();
                    playerScore = 0;
                    enemyScore = 0;
                    check = false;
                    main.setScreen(new ResultScreen(main, false));
                }
            }
        } else {
            if (playerScore >= WIN_SCORE) {
                if (main.history.size() >= 20) {
                    main.history.removeFirst();
                }
                main.history.add(new MatchResult(true, playerScore, enemyScore));
                main.totalWins++;
                main.saveHistory();
                playerScore = 0;
                enemyScore = 0;
                check = false;
                main.setScreen(new ResultScreen(main, true));
            } else if (enemyScore >= WIN_SCORE) {
                if (main.history.size() >= 20) {
                    main.history.removeFirst();
                }
                main.history.add(new MatchResult(false, playerScore, enemyScore));
                main.totalLoses++;
                main.saveHistory();
                playerScore = 0;
                enemyScore = 0;
                check = false;
                main.setScreen(new ResultScreen(main, false));
            }
        }
    }
}
