package com.monopoly.websocet.massage.request.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExcludeLobbyMessage extends WebSocketMessageLobby{
    private UUID playerID;
}
