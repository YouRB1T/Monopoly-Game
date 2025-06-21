package com.monopoly.websocet.massage.request.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateLobbyMessage extends RequestWebSocketMessageLobby {
    private String lobbyName;
    private Integer maxPlayers;
    private String password;
    private Map<String, Object> gameSettings;
}
