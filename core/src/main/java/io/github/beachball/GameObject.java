package io.github.beachball;

import static io.github.beachball.GameSettings.SCALE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GameObject {
    String texturePath; // текстура

    int width; // ширина высота
    int height;
    public short cBits;
    public Body body;
    Texture texture;
    int jumps;
    float angularDamping;
    float linearDamping;
    float restitution;
    public boolean needSetPosition;
    public boolean needSetPositionOne;

    GameObject(int x, int y, int width, int height, World world, String texturePath, short cBits, float angularDamping, float linearDamping, float restitution) {
        jumps = 0;
        this.width = width;
        this.height = height;
        this.cBits = cBits;
        texture = new Texture(texturePath);
        body = createBody(x, y, world, angularDamping, linearDamping, restitution);
        needSetPosition = false;// в конструкторе сразу создаем новое тело
        needSetPositionOne =  false;
    }


    private Body createBody(float x, float y, World world, float angularDamping, float linearDamping, float restitution) {
        BodyDef def = new BodyDef(); // def - defenition (определение) это объект, который содержит все данные, необходимые для посторения тела

        def.type = BodyDef.BodyType.DynamicBody; // тип тела, который имеет массу и может быть подвинут под действием сил
        def.fixedRotation = false; // запрещаем телу вращаться вокруг своей оси
        Body body = world.createBody(def); // создаём в мире world объект по описанному нами определению
        body.setLinearDamping(linearDamping);
        body.setAngularDamping(angularDamping);
        CircleShape circleShape = new CircleShape(); // задаём коллайдер в форме круга
        circleShape.setRadius(Math.max(width, height) * 0.05f / 2f); // определяем радиус круга коллайдера

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape; // устанавливаем коллайдер
        fixtureDef.density = 0.1f; // устанавливаем плотность тела
        fixtureDef.friction = 0.2f; // устанвливаем коэффициент трения
        fixtureDef.restitution = restitution;
        fixtureDef.filter.categoryBits = cBits;


        Fixture fixture = body.createFixture(fixtureDef); // создаём fixture по описанному нами определению
        fixture.setUserData(this);

        circleShape.dispose(); // так как коллайдер уже скопирован в fixutre, то circleShape может быть отчищена, чтобы не забивать оперативную память.

        body.setTransform(x * 0.05f, y * 0.05f, 0); // устанавливаем позицию тела по координатным осям и угол поворота
        return body;

    }

    public void move(float force){
        body.setLinearVelocity(force, body.getLinearVelocity().y);
    }

    public void jump() {
        if (jumps < 2) {
            jumps++;
            body.applyForceToCenter(0, 19000, true);
        }
    }

    public void applyForce(float force){
        body.applyForceToCenter(0, force, true);
    }


    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX() - (width / 2f), getY() - (height / 2f), width, height); // рисуем спрайт
    }

    public void setPosition(){
        needSetPosition = true;

    }
    public void setScore() {
        needSetPositionOne = true;
    }

    public int getY() {
        return (int) (body.getPosition().y / 0.05f);
    }

    public int getX() {
        return (int) (body.getPosition().x / 0.05f);
    }

    public int getJumps() {
        return jumps;
    }

    public void setJumps(int jumps) {
        this.jumps = jumps;
    }

}

