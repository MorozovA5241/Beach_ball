package io.github.beachball;

import static io.github.beachball.GameSettings.BAFFLE_BIT;
import static io.github.beachball.GameSettings.BALL_BIT;
import static io.github.beachball.GameSettings.FLOOR_BIT;
import static io.github.beachball.GameSettings.OBJECT_HEIGHT;
import static io.github.beachball.GameSettings.OBJECT_IMG_PATH;
import static io.github.beachball.GameSettings.OBJECT_WIDTH;
import static io.github.beachball.GameSettings.PLAYER_BIT;
import static io.github.beachball.GameSettings.SCREEN_HEIGHT;
import static io.github.beachball.GameSettings.SCREEN_WIDTH;
import static io.github.beachball.GameSettings.SIMPLE_BIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.beachball.components.ButtonView;
import io.github.beachball.managers.ContactManager;

import com.badlogic.gdx.graphics.g2d.BitmapFont; // что бы шерифт был

import java.util.HashSet;

public class GameScreenOne extends ScreenAdapter {
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
    ButtonView pauseButton;
    ContactManager contactManager;
    GameObject enemy;
    boolean check = false; // Что-бы играть до разнице в очках
    int playerScore = 0;
    int enemyScore = 0;
    final int WIN_SCORE = 11;
    int isPause = -1;
    BitmapFont font;
    public boolean moved = false;
    HashSet<Integer> activePointers = new HashSet<>(); // для мультитача
    public GameScreenOne(Main main) {
        this.main = main;
        main.world = new World(new Vector2(0, -90), true);
        contactManager = new ContactManager(main.world);

        gameObject = new GameObject(SCREEN_WIDTH / 2 - 200, 200, 200, 200 , main.world, "bluePlayer.png", PLAYER_BIT, 1f, 1f, 0f); // делаем динамический объект
        box = new StaticGameObject(SCREEN_WIDTH, 10, OBJECT_WIDTH+ 100000, OBJECT_HEIGHT - 80, main.world, OBJECT_IMG_PATH, FLOOR_BIT);// делаем статический объект
        rightButton = new ButtonView(0, 60, 200, 200, "left.png");
        jumpButton = new ButtonView(1100, 60, 200, 200, "up.png");
        leftButton = new ButtonView(160, 60, 200, 200, "right.png");
        pauseButton = new ButtonView(SCREEN_WIDTH - 200, SCREEN_HEIGHT - 200, 200, 200, "Pause.png");
        wall = new StaticGameObject(SCREEN_WIDTH/2, 30, 20, 700, main.world, "Setka.png", SIMPLE_BIT);
        ball = new GameObject(SCREEN_WIDTH/2 - 100, 600, 60, 60, main.world, "ball.png", BALL_BIT, 1.0f, 1.0f, 1.4f);
        leftSideWall = new StaticGameObject(0, SCREEN_HEIGHT / 2, 3, 5500, main.world, "leftSideWall.png", SIMPLE_BIT);
        rightSideWall = new StaticGameObject(SCREEN_WIDTH, SCREEN_HEIGHT / 2, 3, 5500, main.world, "rightSideWall.png", SIMPLE_BIT);
        baffle = new StaticGameObject(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, 20, 1500, main.world, "Setka.png", BAFFLE_BIT);
        enemy= new GameObject(SCREEN_WIDTH / 2 + 200, 200, 200, 200 , main.world, "redPlayer.png", PLAYER_BIT, 1f, 1f, 0f);
        font = new BitmapFont();
        font.getData().setScale(2);
    }

    @Override
    public void render(float delta) {
        if(isPause == -1) {
            main.stepWorld();
            ball.applyForce(15);
        }
        main.cam.update();
        main.batch.setProjectionMatrix(main.cam.combined);
        ScreenUtils.clear(Color.CLEAR);
        main.batch.begin();
        gameObject.draw(main.batch);
        main.batch.setColor(1, 1, 1, 0.7f); // прозрачность
        rightButton.draw(main.batch);
        leftButton.draw(main.batch);
        jumpButton.draw(main.batch);
        pauseButton.draw(main.batch);
        main.batch.setColor(1, 1, 1, 1); // прозрачность
        enemy.draw(main.batch);
        box.draw(main.batch);
        wall.draw(main.batch);
        ball.draw(main.batch);
        font.draw(main.batch, "You: " + playerScore, 50, SCREEN_HEIGHT - 50);
        font.draw(main.batch, "Enemy: " + enemyScore, SCREEN_WIDTH - 250, SCREEN_HEIGHT - 50);
       // topSideWall.draw(main.batch);
        rightSideWall.draw(main.batch);
        leftSideWall.draw(main.batch);
        main.batch.end(); // рендер(прорисовка кадра)

        handlePauseInput();
        if (ball.needSetPosition == true) {
            main.world.destroyBody(ball.body);
            if (ball.getX() < SCREEN_WIDTH / 2) {
                enemyScore++;
                ball = new GameObject(SCREEN_WIDTH/2 + 100, 500, 60, 60, main.world, "ball.png", BALL_BIT, 1.0f, 1.0f, 1.4f);
            } else {
                playerScore++;
                ball = new GameObject(SCREEN_WIDTH/2 - 100, 500, 60, 60, main.world, "ball.png", BALL_BIT, 1.0f, 1.0f, 1.4f);
            }
            if (enemyScore == playerScore && enemyScore == 10) {
                check = true;
            }
            checkWin(check);

        }
        for (int i = 0; i < 10; i++) {
            handleJumpInput(i);
            handleInput(i);
        }
        enemyLogics();
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
                gameObject.move(35);
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
                    main.history.removeLast();
                }
                main.history.addFirst(new MatchResult(true, playerScore, enemyScore));
                main.totalWins++;
                main.saveHistory();
                playerScore = 0;
                enemyScore = 0;
                check = false;
                destroyAllBodies(main.world);
                main.setScreen(new ResultScreen(main, true, -1));
            } else {
                if (enemyScore - playerScore >= 2) {
                    if (main.history.size() >= 20) {
                        main.history.removeLast();
                    }
                    main.history.addFirst(new MatchResult(false, playerScore, enemyScore));
                    main.totalLoses++;
                    main.saveHistory();
                    playerScore = 0;
                    enemyScore = 0;
                    check = false;
                    destroyAllBodies(main.world);
                    main.setScreen(new ResultScreen(main, false, -1));
                }
            }
        } else {
            if (playerScore >= WIN_SCORE) {
                if (main.history.size() >= 20) {
                    main.history.removeLast();
                }
                main.history.addFirst(new MatchResult(true, playerScore, enemyScore));
                main.totalWins++;
                main.saveHistory();
                playerScore = 0;
                enemyScore = 0;
                check = false;
                destroyAllBodies(main.world);
                main.setScreen(new ResultScreen(main, true, -1));
            } else if (enemyScore >= WIN_SCORE) {
                if (main.history.size() >= 20) {
                    main.history.removeLast();
                }
                main.history.addFirst(new MatchResult(false, playerScore, enemyScore));
                main.totalLoses++;
                main.saveHistory();
                playerScore = 0;
                enemyScore = 0;
                check = false;
                destroyAllBodies(main.world);
                main.setScreen(new ResultScreen(main, false, -1));
            }
        }
    }

    private void enemyLogics(){
        if(ball.getX() < enemy.getX()-40){
            enemy.move(-15);
        }
        else{
            enemy.move(15);
        }
        if(ball.getY() - enemy.getY() <= 120)
            enemy.jump();
    }

    private void handlePauseInput() {
        if (Gdx.input.justTouched()) {
            main.touch = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (pauseButton.isHit(main.touch.x, main.touch.y)) {
                isPause *= -1;
            }

        }
    }

    private void destroyAllBodies(World world) {
        main.world.destroyBody(ball.body);
        main.world.destroyBody(gameObject.body);
        main.world.destroyBody(box.body);
        main.world.destroyBody(leftSideWall.body);
        main.world.destroyBody(rightSideWall.body);
        main.world.destroyBody(wall.body);
        main.world.destroyBody(baffle.body);
        gameObject = new GameObject(-2500, 200, 0, 0 , main.world, "redPlayer.png", PLAYER_BIT, 1f, 1f, 0f); // делаем динамический объект
        box = new StaticGameObject(-2500, 10, 0, 0, main.world, OBJECT_IMG_PATH, FLOOR_BIT);// делаем статический объект
        wall = new StaticGameObject(-2500, 30, 0, 0, main.world, "Setka.png", SIMPLE_BIT);
        ball = new GameObject(-2500, 600, 0, 0, main.world, "ball.png", BALL_BIT, 1.0f, 1.0f, 1.4f);
        leftSideWall = new StaticGameObject(-2500, SCREEN_HEIGHT / 2, 0, 0, main.world, "leftSideWall.png", SIMPLE_BIT);
        rightSideWall = new StaticGameObject(-2500, SCREEN_HEIGHT / 2, 0, 0, main.world, "rightSideWall.png", SIMPLE_BIT);
        baffle = new StaticGameObject(-2500, SCREEN_HEIGHT / 2, 0, 0, main.world, "Setka.png", BAFFLE_BIT);
    }

    @Override
    public void dispose() {
        destroyAllBodies(main.world);
        box.dispose();
        rightButton.dispose();
        jumpButton.dispose();
        leftButton.dispose();
        wall.dispose();
        leftSideWall.dispose();
        rightSideWall.dispose();
        baffle.dispose();
        font.dispose();
    }
}
