package com.monopoly.websocet.massage.session;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.monopoly.websocet.massage.lobby.CloseLobbyMessage;
import com.monopoly.websocet.massage.lobby.CreateLobbyMessage;
import com.monopoly.websocet.massage.lobby.ExcludeLobbyMessage;
import com.monopoly.websocet.massage.lobby.JoinLobbyMessage;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StartGameMessage.class, name = "START_GAME"),
        @JsonSubTypes.Type(value = EndGameMessage.class, name = "END_GAME"),
        @JsonSubTypes.Type(value = GameHandlerMessage.class, name = "HANDLE_EVENT")
})
public abstract class WebSocketMessageSession {
    private UUID sessionId;
    private Long timestamp = System.currentTimeMillis();
}
