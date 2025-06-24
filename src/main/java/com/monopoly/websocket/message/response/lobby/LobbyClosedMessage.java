package com.monopoly.websocket.message.response.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class LobbyClosedMessage extends ResponseWebSocketMessageLobby {
    private UUID lobbyId;
    private String reason;

    public LobbyClosedMessage() {
        setType("LOBBY_CLOSED");
    }

    public LobbyClosedMessage(UUID lobbyId, String reason) {
        this();
        this.lobbyId = lobbyId;
        this.reason = reason;
    }
}

