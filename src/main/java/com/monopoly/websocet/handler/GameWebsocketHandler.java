package com.monopoly.websocet.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monopoly.domain.engine.dto.request.DtoHandlerRequest;
import com.monopoly.domain.engine.dto.response.DtoHandlerResponse;
import com.monopoly.engine.GameSessionEngineService;
import com.monopoly.mapper.RequestGameHandlerMessageMapper;
import com.monopoly.mapper.ResponseGameHandlerMessageMapper;
import com.monopoly.websocet.massage.request.session.RequestGameHandlerMessage;
import com.monopoly.websocet.massage.response.sessoin.ResponseGameHandlerMessage;
import com.monopoly.websocet.massage.response.sessoin.ResponseWebSocketMessageSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameWebsocketHandler implements WebSocketHandler {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RequestGameHandlerMessageMapper messageMapper;
    @Autowired
    private ResponseGameHandlerMessageMapper dtoMapper;
    @Autowired
    private GameSessionEngineService gameSessionEngineService;

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String json = ((TextMessage) message).getPayload();

        RequestGameHandlerMessage requestMessage =
                objectMapper.readValue(json, RequestGameHandlerMessage.class);
        log.info("Received message: {}", requestMessage);
        DtoHandlerRequest dtoRequest = messageMapper.mapToRequest(requestMessage);

        DtoHandlerResponse dtoResponse = gameSessionEngineService.handleGameEvent(dtoRequest);
        log.info("Received message: {}", dtoResponse);
        ResponseGameHandlerMessage response = dtoMapper.mapToResponseHandlerMessage(dtoResponse, (RequestGameHandlerMessage) message);

        broadcastToAll(response);
    }

    protected void broadcastToAll(ResponseWebSocketMessageSession response) throws JsonProcessingException {
        String jsonResponse = objectMapper.writeValueAsString(response);
        log.info("Sending message: {}", jsonResponse);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(jsonResponse));
                } catch (IOException e) {
                }
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("Transport error: {}", exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Connection closed: {}", status.getReason());
        sessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
