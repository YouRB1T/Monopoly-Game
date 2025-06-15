package com.monopoly.websocet.massage.request.session;

import com.monopoly.domain.engine.enums.HandleType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class RequestGameHandlerMessage extends RequestWebSocketMessageSession {
    private HandleType handleType;
    private Map<String, Object> eventData;
}
