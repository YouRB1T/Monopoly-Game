package com.monopoly.websocket.message.response.lobby;

import com.monopoly.domain.engine.Lobby;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class LobbyStateUpdateMessage extends ResponseWebSocketMessageLobby {
    private UUID lobbyId;
    private Lobby lobby;

    public LobbyStateUpdateMessage() {
        setType("LOBBY_STATE_UPDATE");
    }
}