package com.monopoly.websocet.massage.request.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class StartGameMessage extends RequestWebSocketMessageLobby {
    private boolean isClassic;
    private Map<String, Object> initialGameState;
}
