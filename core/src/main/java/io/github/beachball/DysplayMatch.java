package io.github.beachball;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.beachball.components.View;

import com.badlogic.gdx.graphics.g2d.BitmapFont; // что бы шерифт был


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
