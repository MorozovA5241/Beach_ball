package io.github.beachball;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.beachball.AuthInterface;
import io.github.beachball.Main;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class AuthScreen extends ScreenAdapter {
    private final Stage stage;
    private final Main game;
    private final AuthInterface authInterface;
    public AuthScreen(Main game, AuthInterface authInterface) {
        this.game = game;
        this.authInterface = authInterface;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        buildUI();
    }

    private void buildUI() {

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextField emailField = new TextField("", skin);
        emailField.setMessageText("Email");

        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Пароль");
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);

        TextButton loginButton = new TextButton("Войти", skin);
        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                authInterface.login(emailField.getText(), passwordField.getText());
            }
        });

        TextButton registerButton = new TextButton("Зарегистрироваться", skin);
        registerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                authInterface.register(emailField.getText(), passwordField.getText());
            }
        });

        table.add(emailField).width(300).pad(10);
        table.row();
        table.add(passwordField).width(300).pad(10);
        table.row();
        table.add(loginButton).pad(10);
        table.row();
        table.add(registerButton).pad(10);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
