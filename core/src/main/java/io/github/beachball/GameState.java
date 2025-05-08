package io.github.beachball;

public class GameState {
    GameObject player;
    GameObject ball;
    GameState(GameObject player, GameObject ball){
        this.ball = ball;
        this.player = player;
    }

    public GameObject getBall() {
        return ball;
    }

    public GameObject getPlayer() {
        return player;
    }
}
