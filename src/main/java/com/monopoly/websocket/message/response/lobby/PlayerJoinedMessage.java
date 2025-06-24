package com.monopoly.websocket.message.response.lobby;

import com.monopoly.domain.engine.Player;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class PlayerJoinedMessage extends ResponseWebSocketMessageLobby {
    private UUID lobbyId;
    private Player player;

    public PlayerJoinedMessage() {
        setType("PLAYER_JOINED");
    }

    public PlayerJoinedMessage(UUID lobbyId, Player player) {
        this();
        this.lobbyId = lobbyId;
        this.player = player;
    }
}
