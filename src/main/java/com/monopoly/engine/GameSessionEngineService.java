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

    private final Map<Class<? extends DtoHandlerRequest>, CardHandler<?, ?>> handlerMap;

    @Override
    public DtoHandlerResponse handleGameEvent(DtoHandlerRequest request) {
        CardHandler<?, ?> handler = handlerMap.get(request.getClass());

        if (handler == null) {
            throw new IllegalArgumentException("No handler found for request type: " + request.getClass().getSimpleName());
        }

        @SuppressWarnings("unchecked")
        CardHandler<DtoHandlerResponse, DtoHandlerRequest> typedHandler =
                (CardHandler<DtoHandlerResponse, DtoHandlerRequest>) handler;

        return typedHandler.handle(request);
    }
}


