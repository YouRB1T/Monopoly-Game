package com.monopoly.websocket.message.response.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class GameStartedMessage extends ResponseWebSocketMessageLobby {
    private UUID lobbyId;
    private String gameSessionUrl;
    private boolean classic;
    private Map<String, Object> gameState = new HashMap<>();

    public GameStartedMessage() {
        setType("GAME_STARTED");
    }

    public GameStartedMessage(UUID lobbyId, String gameSessionUrl, boolean classic) {
        this();
        this.lobbyId = lobbyId;
        this.gameSessionUrl = gameSessionUrl;
        this.classic = classic;
    }
}
