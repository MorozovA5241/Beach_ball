package io.github.beachball;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.beachball.components.View;

import com.badlogic.gdx.graphics.g2d.BitmapFont; // что бы шерифт был


public class MatchResult {
    boolean who_win;
    int playerScore;
    int enemyScore;

    public MatchResult() {}
    public MatchResult(boolean who_win, int playerScore, int enemyScore) {
        this.who_win = who_win;
        this.playerScore =  playerScore;
        this.enemyScore = enemyScore;
    }

}



//делаем историю матча

