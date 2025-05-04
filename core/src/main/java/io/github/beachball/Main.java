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
    public MenuScreen menuScreen;
    public WinListScreen winlistScreen;
    public RulesScreen rulesScreen;
    public GameScreenOne gameScreenOne;
    public GameScreenTwo gameScreenTwo;
    public LinkedList<MatchResult> history = new LinkedList<>(); //список матчей
    public int totalWins = 0;
    public int totalLoses = 0;
    public boolean mode = false;

    @Override
    public void create() {
        loadHistory();// загружаем историю

        Box2D.init(); //поключаем физику
        world = new World(new Vector2(0, -90), true);
        cam = new OrthographicCamera();//камера
        cam.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);// настройка камеры
        batch = new SpriteBatch(); // штука чтобы отрисовать модель

        menuScreen = new MenuScreen(this);
        setScreen(menuScreen);//ставим экраy
    }

    public void saveHistory() { //сохранение истории в файл
        saveData save = new saveData();
        save.history = history;
        save.totalLosses = totalLoses;
        save.totalWins = totalWins;
        Json json = new Json();
        FileHandle file = Gdx.files.local("history.json");
        file.writeString(json.toJson(save), false);
    }

    public void loadHistory() { //загрузка при входе обратно из файла
        FileHandle file = Gdx.files.local("history.json");
        if (file.exists()) {
            saveData save = new Json().fromJson(saveData.class, file);
            history =  save.history;
            totalWins = save.totalWins;
            totalLoses = save.totalLosses;
        } else {
            history = new LinkedList<>();
            totalLoses = 0;
            totalWins = 0;
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
