package com.monopoly.websocet.massage.request.session;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class RequestEndGameMessage extends RequestWebSocketMessageSession {
    private String reason;
    private Map<String, Object> gameResults;
}
