package com.monopoly.websocet.massage.response.lobby;

import com.monopoly.domain.engine.Lobby;
import com.monopoly.websocet.massage.request.lobby.RequestWebSocketMessageLobby;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LobbyStateUpdateMessage extends RequestWebSocketMessageLobby {
    private Lobby lobby;
}