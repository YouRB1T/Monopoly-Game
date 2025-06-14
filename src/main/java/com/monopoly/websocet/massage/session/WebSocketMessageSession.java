package com.monopoly.websocet.massage.session;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.monopoly.websocet.massage.WebSocketMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StartGameMessage.class, name = "START_GAME"),
        @JsonSubTypes.Type(value = EndGameMessage.class, name = "END_GAME"),
        @JsonSubTypes.Type(value = GameHandlerMessage.class, name = "HANDLE_EVENT")
})
public abstract class WebSocketMessageSession extends WebSocketMessage {
    private UUID sessionId;
}
