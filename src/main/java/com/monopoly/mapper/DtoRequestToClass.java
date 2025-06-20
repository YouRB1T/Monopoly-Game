package com.monopoly.mapper;

import com.monopoly.domain.engine.dto.request.DtoHandlerRequest;
import com.monopoly.engine.handler.card.CardHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DtoRequestToClass {

    public Map<Class<? extends DtoHandlerRequest>, CardHandler<?, ?>> handlerMap(
            List<CardHandler<?, ?>> handlers
    ) {
        return handlers.stream()
                .collect(Collectors.toMap(
                        CardHandler::getSupportedRequestType,
                        Function.identity()
                ));
    }
}
