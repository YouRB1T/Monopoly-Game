package com.monopoly.websocket.message.request.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class StartGameMessage extends RequestWebSocketMessageLobby {
    private UUID lobbyId;
    private boolean classic;
    private Map<String, Object> initialGameState = new HashMap<>();

    public StartGameMessage() {
        setType("START_GAME");
    }
}
