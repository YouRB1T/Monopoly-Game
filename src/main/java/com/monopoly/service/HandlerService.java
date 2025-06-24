package com.monopoly.service;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface HandlerService {
    void handleTextMessage(WebSocketSession session, TextMessage message);
    void handleConnectionClosed(WebSocketSession session, CloseStatus status);
}
