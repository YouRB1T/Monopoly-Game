package com.monopoly.websocket.message.response.sessoin;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ResponseGameHandlerMessage extends ResponseWebSocketMessageSession {
    private String type;
    private Map<String, Object> changedGameSession;

    public ResponseGameHandlerMessage(UUID sessionId, String type, Map<String, Object> changedGameSession) {
        super(sessionId);
        this.type = type;
        this.changedGameSession = changedGameSession;
    }
}
