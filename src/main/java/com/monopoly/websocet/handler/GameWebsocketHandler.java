package com.monopoly.websocet.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;
import com.monopoly.engine.GameSessionEngineService;
import com.monopoly.mapper.RequestGameHandlerMessageMapper;
import com.monopoly.mapper.ResponseGameHandlerMessageMapper;
import com.monopoly.websocet.massage.request.session.RequestGameHandlerMessage;
import com.monopoly.websocet.massage.response.ResponseGameHandlerMessage;
import com.monopoly.websocet.massage.response.ResponseWebSocketMessageSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class GameWebsocketHandler implements WebSocketHandler {
    private final ObjectMapper objectMapper;
    private final RequestGameHandlerMessageMapper messageMapper;
    private final ResponseGameHandlerMessageMapper dtoMapper;
    private final GameSessionEngineService gameSessionEngineService;
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

        DtoHandlerRequest dtoRequest = messageMapper.mapToRequest(requestMessage);

        DtoHandlerResponse dtoResponse = gameSessionEngineService.handleGameEvent(dtoRequest);

        ResponseGameHandlerMessage response = dtoMapper.mapToResponseHandlerMessage(dtoResponse, (RequestGameHandlerMessage) message);

        broadcastToAll(response);
    }

    private void broadcastToAll(ResponseWebSocketMessageSession response) throws JsonProcessingException {
        String jsonResponse = objectMapper.writeValueAsString(response);
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
        // обработка ошибок
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
