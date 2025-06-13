package com.monopoly.websocet.massage.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CloseLobbyMessage extends WebSocketMessageLobby {
    private String reason;
}
