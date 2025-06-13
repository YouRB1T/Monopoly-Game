package com.monopoly.websocet.massage.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class JoinLobbyMessage extends WebSocketMessageLobby {
    private UUID lobbyId;
    private String playerName;
    private String password;
}
