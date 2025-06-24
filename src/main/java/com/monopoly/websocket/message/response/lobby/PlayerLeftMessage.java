package com.monopoly.websocket.message.response.lobby;

import com.monopoly.domain.engine.Player;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class PlayerLeftMessage extends ResponseWebSocketMessageLobby {
    private UUID lobbyId;
    private Player player;
    private String reason;

    public PlayerLeftMessage() {
        setType("PLAYER_LEFT");
    }

    public PlayerLeftMessage(UUID lobbyId, Player player, String reason) {
        this();
        this.lobbyId = lobbyId;
        this.player = player;
        this.reason = reason;
    }
}
