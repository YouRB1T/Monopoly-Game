package com.monopoly.dto.request;

import com.monopoly.domain.engine.Player;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class CreateLobbyRequest {
    private Player creator;
    private String lobbyName;
    private Integer maxPlayers;
    private String password;
    private Map<String, Object> gameRules;
}