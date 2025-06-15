package com.monopoly.engine.config;

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

@Configuration
public class CardHandlerConfig {
    @Bean
    public Map<Class<? extends IDtoCardHandlerRequest>, CardHandler> handlerMap(
            List<CardHandler> handlers
    ) {
        Map<Class<? extends IDtoCardHandlerRequest>, CardHandler> map = new HashMap<>();
        map.put(DtoBuyPropertyRequest.class, getHandler(handlers, BuyPropertyCardHandler.class));
        map.put(DtoChanceHandlerRequest.class, getHandler(handlers, ChanceHandler.class));

        return map;
    }

    private CardHandler getHandler(List<CardHandler> handlers, Class<? extends CardHandler> clazz) {
        return handlers.stream()
                .filter(clazz::isInstance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Handler not found: " + clazz.getSimpleName()));
    }
}
