package com.monopoly.websocet.controller;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;
import com.monopoly.engine.GameSessionEngineService;
import com.monopoly.mapper.GameHandlerMessageMapper;
import com.monopoly.websocet.massage.session.GameHandlerMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@Controller
public class GameWebSocketController {

    @Autowired
    private GameSessionEngineService gameSessionService;
    @Autowired
    private GameHandlerMessageMapper gameSessionMapper;

    @MessageMapping("/game/handle")
    public void handleGameEvent(@DestinationVariable UUID sessionId, GameHandlerMessage message, SimpMessageHeaderAccessor headerAccessor) {
        try {
            log.debug("Обработка события {} для сессии: {}", message.getHandleType(), sessionId);

            DtoHandlerRequest request = gameSessionMapper.map(message);

            DtoHandlerResponse response = gameSessionService.handleGameEvent(request);

            sendGameUpdateToSession(sessionId, response);

            if (response.isGameEnded()) {
                sendToSession(sessionId, new GameEndedEvent(sessionId, response.getEndReason(), response.getGameResults()));
            }

        } catch (GameException e) {
            log.error("Ошибка при обработке события {} для сессии: {}", message.getHandleType(), sessionId, e);
            sendToSession(sessionId, new GameErrorEvent(sessionId, message.getHandleType().name() + "_ERROR", e.getMessage()));
        }
    }

    private void sendToSession(UUID sessionId, Object message) {
        messagingTemplate.convertAndSend("/topic/game/" + sessionId, message);
    }

    private void sendGameUpdateToSession(UUID sessionId, DtoHandlerResponse response) {
        sendToSession(sessionId, new GameStateUpdateEvent(sessionId, response.getUpdatedGameState()));

        // Отправляем все события, которые произошли
        response.getGameEvents().forEach(event -> {
            sendToSession(sessionId, new GameActionEvent(sessionId, event.getEventType(), event.getEventData()));
        });

        // Если есть история действий - отправляем и её
        if (response.getSessionHistory() != null) {
            sendToSession(sessionId, new GameHistoryUpdateEvent(sessionId, response.getSessionHistory()));
        }
    }
}
