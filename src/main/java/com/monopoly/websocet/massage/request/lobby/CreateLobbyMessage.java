package com.monopoly.websocet.massage.request.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateLobbyMessage extends ResponseWebSocketMessageLobby {
    private String lobbyName;
    private Integer maxPlayers;
    private String password;
    private Map<String, Object> gameSettings;
}
