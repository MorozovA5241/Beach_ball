package io.github.beachball;

import com.badlogic.gdx.graphics.Texture;


public class DysplayMatch {
    float x;
    float y;
    MatchResult result;
    Texture texture;
    public DysplayMatch () {}
    public DysplayMatch (float x, float y, MatchResult result, Texture texture) {
        this.texture = texture;
        this.result = result;
        this.x = x;
        this.y = y;
    }
}
