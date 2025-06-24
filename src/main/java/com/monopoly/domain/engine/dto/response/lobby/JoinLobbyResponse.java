package com.monopoly.domain.engine.dto.response.lobby;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.monopoly.domain.engine.Lobby;
import com.monopoly.domain.engine.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class JoinLobbyResponse {
    @JsonProperty("lobby_id")
    private UUID lobbyId;
    
    @JsonProperty("player")
    private Player player;
    
    @JsonProperty("lobby")
    private Lobby lobby;
    
    @JsonProperty("websocket_url")
    private String websocketUrl;
}