package com.crocoby;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpInfoServer implements ModInitializer {
	public static final String MODID = "httpinfoserver";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static boolean connectedWebSocket = false; // The state of WebSocket client (uses to send message to the chat for minecraft client)

	// Entrypoint, starts when minecraft loads
	@Override
	public void onInitialize() {
		// Hello message in minecraft process console
		LOGGER.info("Loaded!");

		// Start websocket client and connect to server
		new WebSocketDoor().connect();
	}
}