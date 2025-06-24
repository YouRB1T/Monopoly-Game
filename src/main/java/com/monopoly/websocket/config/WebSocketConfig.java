package com.monopoly.websocket.config;

import com.monopoly.websocket.handler.GameWebsocketHandler;
import com.monopoly.websocket.handler.LobbyWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getGameWebSocketHandler(), "/game/session")
                .setAllowedOrigins("*")
                .withSockJS();

        registry.addHandler(getLobbyWebSocketHandler(), "/game/lobby")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Bean
    public WebSocketHandler getGameWebSocketHandler() {
        return new GameWebsocketHandler();
    }

    @Bean
    public WebSocketHandler getLobbyWebSocketHandler() {
        return new LobbyWebSocketHandler();
    }
}