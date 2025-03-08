package io.github.beachball;

import static io.github.beachball.GameSettings.SCALE;
import static io.github.beachball.GameSettings.SCREEN_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class StaticGameObject {

    String texturePath;

    int width;
    int height;

    Body body;
    Texture texture;
    StaticGameObject(int x, int y, int width, int height, World world, String texturePath) {
        this.width = width;
        this.height = height;

        texture = new Texture(texturePath);
        body = createStaticBody(x, y, world);
    }

    private Body createStaticBody(float x, float y, World world){
        BodyDef def = new BodyDef(); // def - defenition (определение) это объект, который содержит все данные, необходимые для посторения тела

        def.type = BodyDef.BodyType.StaticBody; // тип тела, который просто есть
        def.fixedRotation = true; // запрещаем телу вращаться вокруг своей оси
        Body body = world.createBody(def); // создаём в мире world объект по описанному нами определению

        PolygonShape boxShape = new PolygonShape(); // это надо поменять на прямоугольник
        boxShape.setAsBox(1000,2 ); //

        FixtureDef fixtureDef = new FixtureDef();
        body.createFixture(boxShape, 0.0f);
        boxShape.dispose();
        return body;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX() - (width / 2f), getY() - (height / 2f), width, height);
    }

    private int getY() {
        return (int) (body.getPosition().y / SCALE);
    }

    private int getX() {
        return (int) (body.getPosition().x / SCALE);
    }

}
