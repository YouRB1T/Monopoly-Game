package com.monopoly.websocket.message;

import lombok.Data;

@Data
public abstract class WebSocketMessage {
    private Long timestamp = System.currentTimeMillis();
}
