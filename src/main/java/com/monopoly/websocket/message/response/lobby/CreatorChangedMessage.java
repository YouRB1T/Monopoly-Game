package com.monopoly.websocket.message.response.lobby;

import com.monopoly.domain.engine.Player;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreatorChangedMessage extends ResponseWebSocketMessageLobby {
    private UUID lobbyId;
    private Player newCreator;
    private Player previousCreator;

    public CreatorChangedMessage() {
        setType("CREATOR_CHANGED");
    }

    public CreatorChangedMessage(UUID lobbyId, Player newCreator, Player previousCreator) {
        this();
        this.lobbyId = lobbyId;
        this.newCreator = newCreator;
        this.previousCreator = previousCreator;
    }
}
