package com.monopoly.websocket.message.response.lobby;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LobbyStateUpdateMessage.class, name = "LOBBY_STATE_UPDATE"),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = "ERROR"),
        @JsonSubTypes.Type(value = LobbyClosedMessage.class, name = "LOBBY_CLOSED"),
        @JsonSubTypes.Type(value = PlayerExcludedMessage.class, name = "PLAYER_EXCLUDED"),
        @JsonSubTypes.Type(value = GameStartedMessage.class, name = "GAME_STARTED")
})
@Data
public abstract class ResponseWebSocketMessageLobby {
    private String type;
}
