package com.monopoly.websocet.massage.request.lobby;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.monopoly.websocet.massage.WebSocketMessage;
import com.monopoly.websocet.massage.response.lobby.LobbyStateUpdateMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateLobbyMessage.class, name = "CREATE_LOBBY"),
        @JsonSubTypes.Type(value = JoinLobbyMessage.class, name = "JOIN_LOBBY"),
        @JsonSubTypes.Type(value = CloseLobbyMessage.class, name = "CLOSE_LOBBY"),
        @JsonSubTypes.Type(value = ExcludeLobbyMessage.class, name = "EXCLUDE_LOBBY"),
        @JsonSubTypes.Type(value = StartGameMessage.class, name = "START_GAME"),
        @JsonSubTypes.Type(value = UpdateLobbyPasswordMessage.class, name = "UPDATE_PASSWORD"),
        @JsonSubTypes.Type(value = LobbyStateUpdateMessage.class, name = "LOBBY_STATE_UPDATE"),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = "ERROR")
})
public abstract class ResponseWebSocketMessageLobby extends WebSocketMessage {
    private UUID lobbyId;
}

