package com.monopoly.websocket.message.request.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class CloseLobbyMessage extends RequestWebSocketMessageLobby {
    private UUID lobbyId;

    public CloseLobbyMessage() {
        setType("CLOSE_LOBBY");
    }
}
