package com.monopoly.websocet.massage.lobby;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.monopoly.websocet.massage.WebSocketMessage;
import lombok.Data;

import java.util.UUID;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateLobbyMessage.class, name = "CREATE_LOBBY"),
        @JsonSubTypes.Type(value = JoinLobbyMessage.class, name = "JOIN_LOBBY"),
        @JsonSubTypes.Type(value = CloseLobbyMessage.class, name = "CLOSE_LOBBY"),
        @JsonSubTypes.Type(value = ExcludeLobbyMessage.class, name = "EXCLUDE_LOBBY")
})
public abstract class WebSocketMessageLobby extends WebSocketMessage {
    private UUID lobbyId;
    private Long timestamp = System.currentTimeMillis();
}

