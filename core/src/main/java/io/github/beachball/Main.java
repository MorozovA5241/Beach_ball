package io.github.beachball;

import static io.github.beachball.GameSettings.POSITION_ITERATIONS;
import static io.github.beachball.GameSettings.SCREEN_HEIGHT;
import static io.github.beachball.GameSettings.SCREEN_WIDTH;
import static io.github.beachball.GameSettings.STEP_TIME;
import static io.github.beachball.GameSettings.VELOCITY_ITERATIONS;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.physics.box2d.Box2D;

import com.badlogic.gdx.physics.box2d.World;

import java.util.LinkedList;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.files.FileHandle; // для истории матчей


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public World world;
    OrthographicCamera cam;
    float accumulator = 0;

    public SpriteBatch batch;

    public SpriteBatch batch1;
    Vector3 touch;
    public GameScreen gameScreen;
    public MenuScreen menuScreen;
    public WinListScreen winlistScreen;

    public LinkedList<MatchResult> history = new LinkedList<>(); //список матчей


    @Override
    public void create() {
        loadHistory();// загружаем историю

        Box2D.init(); //поключаем физику
        world = new World(new Vector2(0, -90), true);  // делаем мир
        cam = new OrthographicCamera();//камера
        cam.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);// настройка камеры
        batch = new SpriteBatch(); // штука чтобы отрисовать модель
        gameScreen = new GameScreen(this);//ставим экран
        winlistScreen = new WinListScreen(this);

        menuScreen = new MenuScreen(this);
        setScreen(menuScreen);//ставим экраy
    }

    public void saveHistory() { //сохранение истории в файл
        Json json = new Json();
        FileHandle file = Gdx.files.local("history.json");
        file.writeString(json.toJson(history), false);
    }

    public void loadHistory() { //загрузка при входе обратно из файла
        FileHandle file = Gdx.files.local("history.json");
        if (file.exists()) {
            Json json = new Json();
            history = json.fromJson(LinkedList.class, MatchResult.class, file);
        } else {
            history = new LinkedList<>();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        batch1.dispose();       // удаление не нужных объектов работает само
    }

    public void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += delta;

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS); // функция просчитывает один шаг физики  |   нужно чтобы не было рассинхрона
        }
    }

}
