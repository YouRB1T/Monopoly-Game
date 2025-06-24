package com.monopoly.domain.engine.dto.request.lobby;

import lombok.Data;

import java.util.UUID;

@Data
public class JoinLobbyRequest {
    private String playerName;
    private String password;
}