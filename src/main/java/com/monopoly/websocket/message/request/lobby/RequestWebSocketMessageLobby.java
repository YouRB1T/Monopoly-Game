package com.monopoly.websocket.message.request.lobby;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.monopoly.websocket.message.WebSocketMessage;
import lombok.Data;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CloseLobbyMessage.class, name = "CLOSE_LOBBY"),
        @JsonSubTypes.Type(value = ExcludeLobbyMessage.class, name = "EXCLUDE_PLAYER"),
        @JsonSubTypes.Type(value = StartGameMessage.class, name = "START_GAME"),
        @JsonSubTypes.Type(value = UpdateLobbyPasswordMessage.class, name = "UPDATE_PASSWORD")
})
@Data
public abstract class RequestWebSocketMessageLobby extends WebSocketMessage {
    private String type;
}

