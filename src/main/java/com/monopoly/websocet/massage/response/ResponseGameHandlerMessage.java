package com.monopoly.websocet.massage.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ResponseGameHandlerMessage extends ResponseWebSocketMessageSession{
    private String type;
    private Map<String, Object> changedGameSession;

    public ResponseGameHandlerMessage(UUID sessionId, String type, Map<String, Object> changedGameSession) {
        super(sessionId);
        this.type = type;
        this.changedGameSession = changedGameSession;
    }
}
