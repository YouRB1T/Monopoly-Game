package com.monopoly.websocet.massage.session;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class StartGameMessage extends WebSocketMessageSession {
    private UUID lobbyId;
    private Map<String, Object> initialGameState;
}
