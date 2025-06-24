package com.monopoly.websocket.handler;

import com.monopoly.service.HandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class LobbyWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private HandlerService lobbyHandlerService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("New lobby connection established: {}", session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        lobbyHandlerService.handleTextMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("Transport error for lobby session {}: {}", session.getId(), exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        lobbyHandlerService.handleConnectionClosed(session, status);
    }
}