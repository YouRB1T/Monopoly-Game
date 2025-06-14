package com.monopoly.engine;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.request.card.DtoBuyPropertyRequest;
import com.monopoly.domain.dto.request.card.DtoChanceHandlerRequest;
import com.monopoly.domain.dto.request.card.IDtoCardHandlerRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;
import com.monopoly.domain.dto.response.card.IDtoCardHandlerResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.engine.handler.card.BuyPropertyCardHandler;
import com.monopoly.engine.handler.card.CardHandler;
import com.monopoly.engine.handler.card.ChanceHandler;
import com.monopoly.repository.GameSessionRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameSessionEngineService implements GameSessionEngine {

    private final Map<Class<? extends IDtoCardHandlerRequest>, CardHandler> handlerMap;
    private final GameSessionRedisRepository gameSessionRepository;

    @Override
    public DtoHandlerResponse handleGameEvent(DtoHandlerRequest request) {

        CardHandler handler = handlerMap.get(request.getClass());
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for request type: " + request.getClass().getSimpleName());
        }

        // Загружаем сессию
        UUID sessionId = dtoRequest.getGameSessionId();
        GameSession gameSession = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("GameSession not found: " + sessionId));

        // Устанавливаем нужные поля в handler
        injectFields(handler, request, gameSession);

        // Вызываем обработку
        handler.handle();

        // Возвращаем результат
        return new DtoHandlerResponse(/* можешь добавить статус или response-объект */);
    }

    private IDtoCardHandlerResponse injectFields(CardHandler handler, IDtoCardHandlerRequest request, GameSession gameSession) {
        if (handler instanceof BuyPropertyCardHandler && request instanceof DtoBuyPropertyRequest dto) {
            BuyPropertyCardHandler h = (BuyPropertyCardHandler) handler;
            h.setPlayer(gameSession.getCurrentPlayer());
            h.setCard(gameSession.getBoard().getPropertyById(dto.getPropertyId()));
        }

        if (handler instanceof ChanceHandler && request instanceof DtoChanceHandlerRequest) {
            ChanceHandler h = (ChanceHandler) handler;
            h.setPlayer(gameSession.getCurrentPlayer());
            h.setPrisonCard(gameSession.getCardDeck().getRandomPrisonCard());
        }

        // Добавь остальные if-ветки для других Handler'ов (PayRentHandler, GoToPrisonHandler и т.д.)
    }
}

