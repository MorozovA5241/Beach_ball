package io.github.beachball;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MyWebSocketClient extends WebSocketClient {

    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Соединение установлено");
        send("Привет, сервер!");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Ответ от сервера: " + message);
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
