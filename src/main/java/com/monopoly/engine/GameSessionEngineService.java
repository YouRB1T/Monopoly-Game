package com.monopoly.engine;

import com.monopoly.domain.engine.dto.request.DtoHandlerRequest;
import com.monopoly.domain.engine.dto.request.card.IDtoCardHandlerRequest;
import com.monopoly.domain.engine.dto.response.DtoHandlerResponse;
import com.monopoly.domain.engine.dto.response.card.IDtoCardHandlerResponse;
import com.monopoly.engine.handler.card.CardHandler;
import com.monopoly.mapper.DtoRequestToClass;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Data
@RequiredArgsConstructor
public class GameSessionEngineService implements GameSessionEngine {

    @Autowired
    private DtoRequestToClass requestToClassMapper;

    @Override
    public DtoHandlerResponse handleGameEvent(DtoHandlerRequest request) {
        CardHandler<?, ?> handler = requestToClassMapper.getHandlerMap().get(request.getClass());

        if (handler == null) {
            throw new IllegalArgumentException("No handler found for request type: " + request.getClass().getSimpleName());
        }

        @SuppressWarnings("unchecked")
        CardHandler<IDtoCardHandlerResponse, IDtoCardHandlerRequest> typedHandler =
                (CardHandler<IDtoCardHandlerResponse, IDtoCardHandlerRequest>) handler;

        return typedHandler.handle((IDtoCardHandlerRequest) request);
    }
}


