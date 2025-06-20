package com.monopoly.dto.response;

import com.monopoly.domain.engine.Lobby;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateLobbyResponse {
    private UUID lobbyId;
    private Lobby lobby;
    private String websocketUrl;
}