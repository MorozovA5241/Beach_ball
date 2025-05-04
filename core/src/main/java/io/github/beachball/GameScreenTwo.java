package io.github.beachball;

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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.beachball.components.ButtonView;
import io.github.beachball.managers.ContactManager;

import com.badlogic.gdx.graphics.g2d.BitmapFont; // что бы шерифт был

import java.util.HashSet;

public class GameScreenTwo extends ScreenAdapter {
    Main main;
    GameObject gameObject;
    StaticGameObject box;
    StaticGameObject rightSideWall;
    StaticGameObject leftSideWall;
    GameObject ball;
    ButtonView rightButton;
    ButtonView leftButton;
    ButtonView jumpButton;
    ContactManager contactManager;
    int Score = 0;
    BitmapFont font;
    public boolean moved = false;
    HashSet<Integer> activePointers = new HashSet<>(); // для мультитача
    public GameScreenTwo(Main main) {
        this.main = main;
        Score = 0;
        contactManager = new ContactManager(main.world);

        gameObject = new GameObject(SCREEN_WIDTH / 2 - 200, 200, 200, 200 , main.world, "redPlayer.png", PLAYER_BIT, 1f, 1f, 0f); // делаем динамический объект
        box = new StaticGameObject(SCREEN_WIDTH, 10, OBJECT_WIDTH+ 100000, OBJECT_HEIGHT - 80, main.world, OBJECT_IMG_PATH, FLOOR_BIT);// делаем статический объект
        rightButton = new ButtonView(0, 60, 200, 200, "left.png");
        jumpButton = new ButtonView(1100, 60, 200, 200, "up.png");
        leftButton = new ButtonView(160, 60, 200, 200, "right.png");
        ball = new GameObject(SCREEN_WIDTH/2 - 100, 600, 60, 60, main.world, "ball.png", BALL_BIT, 1.0f, 1.0f, 1.4f);
        //topSideWall = new StaticGameObject(SCREEN_WIDTH / 2, SCREEN_HEIGHT, 3000, 80, main.world, OBJECT_IMG_PATH, SIMPLE_BIT); //под вопросом
        leftSideWall = new StaticGameObject(300, SCREEN_HEIGHT / 2, 3, 5500, main.world, "leftSideWall.png", SIMPLE_BIT);
        rightSideWall = new StaticGameObject(SCREEN_WIDTH - 300, SCREEN_HEIGHT / 2, 3, 5500, main.world, "rightSideWall.png", SIMPLE_BIT);
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
        ball.draw(main.batch);
        font.draw(main.batch, "Score: " + Score, 50, SCREEN_HEIGHT - 50);
        rightSideWall.draw(main.batch);
        leftSideWall.draw(main.batch);
        main.batch.end(); // рендер(прорисовка кадра)
        ball.applyForce(20); // чтобы мячик был легче

        if (ball.needSetPositionOne == true) {
            Score++;
            ball.needSetPositionOne = false;
        }
        if (ball.needSetPosition == true) {
            ball.needSetPosition = false;
            destroyAllBodies(main.world);
            main.setScreen(new ResultScreen(main, true, Score));
        }
        for (int i = 0; i < 10; i++) {
            handleJumpInput(i);
            handleMoveInput(i);
        }
        if (!moved) {
            gameObject.move(0);
        }
        moved = false;
    }

    private void handleMoveInput(int i) {
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

    private void destroyAllBodies(World world) {
        main.world.destroyBody(ball.body);
        main.world.destroyBody(gameObject.body);
        main.world.destroyBody(box.body);
        main.world.destroyBody(leftSideWall.body);
        main.world.destroyBody(rightSideWall.body);
        gameObject = new GameObject(-2500, 200, 0, 0 , main.world, "redPlayer.png", PLAYER_BIT, 1f, 1f, 0f); // делаем динамический объект
        box = new StaticGameObject(-2500, 10, 0, 0, main.world, OBJECT_IMG_PATH, FLOOR_BIT);// делаем статический объект
        ball = new GameObject(-2500, 600, 0, 0, main.world, "ball.png", BALL_BIT, 1.0f, 1.0f, 1.4f);
        leftSideWall = new StaticGameObject(-2500, SCREEN_HEIGHT / 2, 0, 0, main.world, "leftSideWall.png", SIMPLE_BIT);
        rightSideWall = new StaticGameObject(-2500, SCREEN_HEIGHT / 2, 0, 0, main.world, "rightSideWall.png", SIMPLE_BIT);
    }

    @Override
    public void dispose() {
        destroyAllBodies(main.world);
        box.dispose();
        rightButton.dispose();
        jumpButton.dispose();
        leftButton.dispose();
        leftSideWall.dispose();
        rightSideWall.dispose();
        font.dispose();
    }
}
