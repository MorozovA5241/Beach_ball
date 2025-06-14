package io.github.beachball.components;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class MyView implements Disposable {

    protected float x;
    protected float y;
    public float width;
    protected float height;

    public MyView(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isHit(float tx, float ty) {
        return (tx >= x && tx <= x + width && ty >= y && ty <= y + height);
    }

    public void draw(SpriteBatch batch) {
    }


    @Override
    public void dispose() {

    }
}
