package io.github.beachball;
import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MyWebSocketClient extends WebSocketClient {

    private static final Gson gson = new Gson();
    public GameScreenOne screenOne;
    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("Client connected");
    }

    @Override
    public void onMessage(String message) {
        GameState gameStateFrom = gson.fromJson(message, GameState.class);
        System.out.println("Ответ от сервера: " + message);
        GameState gameStateTo = new GameState(screenOne.gameObject, screenOne.ball);
        send(gson.toJson(gameStateTo));

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Соединение закрыто");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }




}
