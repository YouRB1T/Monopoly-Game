package com.monopoly.websocet.massage.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateLobbyMessage extends WebSocketMessageLobby {
    private String lobbyName;
    private Integer maxPlayers;
    private String password;
    private Map<String, Object> gameSettings;
}
