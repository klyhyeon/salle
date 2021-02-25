package com.example.salle.application;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyTextWebSocketHandler extends TextWebSocketHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyTextWebSocketHandler.class);
	
	private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
		LOGGER.info("접속: {}	", session);
		super.afterConnectionEstablished(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
		super.afterConnectionClosed(session, status);
	}

	//invokes when websocket message arrives
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);
		sessions.forEach(webSocketSession -> {
			try {
				webSocketSession.sendMessage(message);
				LOGGER.info("handleTextMessage arrived");
			} catch(IOException e) {
				LOGGER.error("handleTextMessage error occurred", e);
			}
		});
	}
	
	

	
	

}
