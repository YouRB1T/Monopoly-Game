package com.monopoly.engine.config;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.request.card.DtoBuyPropertyRequest;
import com.monopoly.domain.dto.request.card.DtoChanceHandlerRequest;
import com.monopoly.domain.dto.request.card.IDtoCardHandlerRequest;
import com.monopoly.engine.handler.card.BuyPropertyCardHandler;
import com.monopoly.engine.handler.card.CardHandler;
import com.monopoly.engine.handler.card.ChanceHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class CardHandlerConfig {

    @Bean
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
