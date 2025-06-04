package io.github.beachball.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ButtonView extends MyView {

    Texture texture;

    public ButtonView(float x, float y, float width, float height, String texturePath) {
        super(x, y, width, height);

        texture = new Texture(texturePath);
    }
    @Override
    public void draw(SpriteBatch batch){
        batch.draw(texture, x, y, width, height);
    }

    @Override
    public void dispose(){
        texture.dispose();
    }
}
