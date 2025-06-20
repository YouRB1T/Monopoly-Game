package com.monopoly.websocet.massage.response.sessoin;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.monopoly.websocet.massage.WebSocketMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ResponseEndGameMessage.class, name = "END_GAME"),
        @JsonSubTypes.Type(value = ResponseGameHandlerMessage.class, name = "HANDLE_EVENT")
})
public abstract class ResponseWebSocketMessageSession extends WebSocketMessage {
    protected UUID sessionId;
}
