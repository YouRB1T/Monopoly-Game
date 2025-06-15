package com.monopoly.websocet.massage.request.session;

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
        @JsonSubTypes.Type(value = RequestEndGameMessage.class, name = "END_GAME"),
        @JsonSubTypes.Type(value = RequestGameHandlerMessage.class, name = "HANDLE_EVENT")
})
public abstract class RequestWebSocketMessageSession extends WebSocketMessage {
    private UUID sessionId;
}
