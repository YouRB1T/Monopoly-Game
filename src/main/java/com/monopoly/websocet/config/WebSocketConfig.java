package com.monopoly.websocet.config;

import com.monopoly.websocet.handler.GameWebsocketHandler;
import com.monopoly.websocet.handler.LobbyWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private GameWebsocketHandler gameHandler;
    
    @Autowired
    private LobbyWebSocketHandler lobbyHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Регистрируем обработчик для игровых сессий
        registry.addHandler(gameHandler, "/game/session/{sessionId}")
                .setAllowedOrigins("*")
                .withSockJS();
        
        // Регистрируем обработчик для лобби
        registry.addHandler(lobbyHandler, "/game//lobby/{lobbyId}")
                .setAllowedOrigins("*")
                .withSockJS();
    }
}