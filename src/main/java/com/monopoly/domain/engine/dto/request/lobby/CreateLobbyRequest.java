package com.monopoly.domain.engine.dto.request.lobby;

import com.monopoly.domain.entity.PlayerEntity;
import lombok.Data;

import java.util.Map;

@Data
public class CreateLobbyRequest {
    private PlayerEntity creator;
    private String lobbyName;
    private Integer maxPlayers;
    private String password;
    private Map<String, Object> gameRules;
}