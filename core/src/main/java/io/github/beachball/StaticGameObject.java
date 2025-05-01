package io.github.beachball;

import static io.github.beachball.GameSettings.SCALE;
import static io.github.beachball.GameSettings.SCREEN_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class StaticGameObject {


    String texturePath;

    int width;   // в пикселях
    int height;  // в пикселях
    public short cBits;

    Body body;
    Texture texture;
    public StaticGameObject(int x, int y, int width, int height, World world, String texturePath, short cBits) {
        this.width = width;
        this.height = height;
        this.cBits = cBits;
        this.texturePath = texturePath;

        texture = new Texture(texturePath);
        body = createStaticBody(x, y, world, width, height);
    }

    private Body createStaticBody(float x, float y, World world, int width, int height) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.fixedRotation = true;

        // Переводим координаты в метры
        def.position.set(x * SCALE, y * SCALE);
        Body body = world.createBody(def);

        PolygonShape boxShape = new PolygonShape();

        // ВАЖНО: setAsBox принимает половинные размеры в метрах
        boxShape.setAsBox((width / 2f) * SCALE, (height / 2f) * SCALE);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.filter.categoryBits = cBits;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        boxShape.dispose();

        return body;
    }

    public void draw(SpriteBatch batch) {

        batch.draw(texture,
            getX() - width / 2f,
            getY() - height / 2f,
            width, height);
    }

    public void dispose() {
        texture.dispose();
    }

    public int getX() {
        return (int) (body.getPosition().x / SCALE);
    }

    public int getY() {
        return (int) (body.getPosition().y / SCALE);
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }
}
