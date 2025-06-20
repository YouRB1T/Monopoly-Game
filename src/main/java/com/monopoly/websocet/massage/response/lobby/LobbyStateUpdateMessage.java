package com.monopoly.websocet.massage.response.lobby;

import com.monopoly.domain.engine.Lobby;
import com.monopoly.websocet.massage.request.lobby.ResponseWebSocketMessageLobby;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LobbyStateUpdateMessage extends ResponseWebSocketMessageLobby {
    private Lobby lobby;
}