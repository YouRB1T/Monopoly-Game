package com.monopoly.mapper;

import com.monopoly.domain.engine.dto.request.DtoHandlerRequest;
import com.monopoly.engine.handler.card.CardHandler;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DtoRequestToClass {

    private final List<CardHandler<?, ?>> handlers;

    @Getter
    private Map<Class<? extends DtoHandlerRequest>, CardHandler<?, ?>> handlerMap;

    @PostConstruct
    public void init() {
        handlerMap = handlers.stream()
                .collect(Collectors.toMap(
                        CardHandler::getSupportedRequestType,
                        Function.identity()
                ));
    }
}
