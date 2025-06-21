package com.monopoly.engine;

import com.monopoly.domain.engine.dto.request.DtoHandlerRequest;
import com.monopoly.domain.engine.dto.response.DtoHandlerResponse;
import com.monopoly.engine.handler.card.CardHandler;
import com.monopoly.mapper.DtoRequestToClass;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Data
@RequiredArgsConstructor
public class GameSessionEngineService implements GameSessionEngine {

    @Autowired
    private DtoRequestToClass requestToClassMapper;

    private final Map<Class<? extends DtoHandlerRequest>, CardHandler<?, ?>> handlerMap;

    @Override
    public DtoHandlerResponse handleGameEvent(DtoHandlerRequest request) {
        handlerMap = requestToClassMapper.handlerMap();
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


