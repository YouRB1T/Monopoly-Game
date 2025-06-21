package com.monopoly.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.monopoly.domain.engine.Lobby;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateLobbyResponse {
    @JsonProperty("lobby_id")
    private UUID lobbyId;
    @JsonProperty("lobby")
    private Lobby lobby;
    @JsonProperty("websocket_url")
    private String websocketUrl;
}