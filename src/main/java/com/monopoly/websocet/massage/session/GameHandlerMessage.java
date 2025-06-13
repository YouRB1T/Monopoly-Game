package com.monopoly.websocet.massage.session;

import com.monopoly.domain.engine.enums.HandleType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class GameHandlerMessage extends WebSocketMessageSession {
    private HandleType handleType;
    private Map<String, Object> eventData;
}
