package io.github.beachball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GameObject {
    String texturePath; // текстура

    int width; // ширина высота
    int height;

    Body body;
    Texture texture;
    GameObject(int x, int y, int width, int height, World world, String texturePath) {
        this.width = width;
        this.height = height;

        texture = new Texture(texturePath);
        body = createBody(x, y, world); // в конструкторе сразу создаем новое тело
    }

    private Body createBody(float x, float y, World world) {
        BodyDef def = new BodyDef(); // def - defenition (определение) это объект, который содержит все данные, необходимые для посторения тела

        def.type = BodyDef.BodyType.DynamicBody; // тип тела, который имеет массу и может быть подвинут под действием сил
        def.fixedRotation = false; // запрещаем телу вращаться вокруг своей оси
        Body body = world.createBody(def); // создаём в мире world объект по описанному нами определению

        CircleShape circleShape = new CircleShape(); // задаём коллайдер в форме круга
        circleShape.setRadius(Math.max(width, height) * 0.05f / 2f); // определяем радиус круга коллайдера

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape; // устанавливаем коллайдер
        fixtureDef.density = 0.1f; // устанавливаем плотность тела
        fixtureDef.friction = 1000f; // устанвливаем коэффициент трения

        body.createFixture(fixtureDef); // создаём fixture по описанному нами определению
        circleShape.dispose(); // так как коллайдер уже скопирован в fixutre, то circleShape может быть отчищена, чтобы не забивать оперативную память.

        body.setTransform(x * 0.05f, y * 0.05f, 0); // устанавливаем позицию тела по координатным осям и угол поворота
        return body;

    }

    public void move(float force){
        body.applyForceToCenter(force, 0, true);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX() - (width / 2f), getY() - (height / 2f), width, height); // рисуем спрайт
    }

    public int getY() {
        return (int) (body.getPosition().y / 0.05f);
    }

    public int getX() {
        return (int) (body.getPosition().x / 0.05f);
    }

}
