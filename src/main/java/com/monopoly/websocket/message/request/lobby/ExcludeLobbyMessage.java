package com.monopoly.websocket.message.request.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExcludeLobbyMessage extends RequestWebSocketMessageLobby {
    private UUID lobbyId;
    private UUID playerID;

    public ExcludeLobbyMessage() {
        setType("EXCLUDE_PLAYER");
    }
}
