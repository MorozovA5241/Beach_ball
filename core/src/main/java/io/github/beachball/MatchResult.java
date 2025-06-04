package io.github.beachball;


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

