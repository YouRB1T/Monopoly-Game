package com.monopoly.websocet.massage.request.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class JoinLobbyMessage extends RequestWebSocketMessageLobby {
    private UUID lobbyId;
    private String playerName;
    private String password;
}
