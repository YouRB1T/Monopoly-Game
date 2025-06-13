package com.monopoly.websocet.massage.session;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class EndGameMessage extends WebSocketMessageSession {
    private UUID gameSessionId;
    private String reason;
    private Map<String, Object> gameResults;
}
