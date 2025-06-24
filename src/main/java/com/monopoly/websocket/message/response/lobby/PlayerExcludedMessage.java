package com.monopoly.websocket.message.response.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class PlayerExcludedMessage extends ResponseWebSocketMessageLobby {
    private UUID lobbyId;
    private UUID excludedPlayerId;
    private String reason;

    public PlayerExcludedMessage() {
        setType("PLAYER_EXCLUDED");
    }

    public PlayerExcludedMessage(UUID lobbyId, UUID excludedPlayerId, String reason) {
        this();
        this.lobbyId = lobbyId;
        this.excludedPlayerId = excludedPlayerId;
        this.reason = reason;
    }
}
