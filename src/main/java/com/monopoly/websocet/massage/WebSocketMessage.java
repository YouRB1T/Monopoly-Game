package com.monopoly.websocet.massage;

import lombok.Data;

@Data
public abstract class WebSocketMessage {
    private Long timestamp = System.currentTimeMillis();
}
