package com.crocoby;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketDoor extends WebSocketClient {
    int connectTries = 0;

    public WebSocketDoor() {
        // Connecting to the WebSocket server

        super(URI.create("ws://127.0.0.1:9922/connectClient"));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // When connected successfully

        connectTries = 0; // Reset connect tries count
        HttpInfoServer.connectedWebSocket = true;

        HttpInfoServer.LOGGER.info("[WebSocket] Connected");
    }

    @Override
    public void onMessage(String message) {
        // When message received

        if (message.equals("collectData")) {
            // Server wants to give data

            send(InfoCollector.genJson());
        } else {
            send("unknownRequest");
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // When disconnected from server (also in the case when client have not been connected to the server)

        HttpInfoServer.LOGGER.info("[WebSocket] Disconnected");
        HttpInfoServer.connectedWebSocket = false;

        // Re-connection
        connectTries++;
        try {
            Thread.sleep(Math.min((long) connectTries * connectTries * 1000, 300000));
        } catch (InterruptedException ignored) {}

        HttpInfoServer.LOGGER.info("[WebSocket] Reconnection (attempt #"+connectTries+")");
        new Thread(this::reconnect).start();
    }

    @Override
    public void onError(Exception ex) {
        // When error happened

        HttpInfoServer.LOGGER.error("[WebSocket] error: "+ex.getMessage());
    }
}
