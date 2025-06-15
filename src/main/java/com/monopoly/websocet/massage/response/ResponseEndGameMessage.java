package com.monopoly.websocet.massage.response;

import com.monopoly.domain.engine.Player;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResponseEndGameMessage extends ResponseWebSocketMessageSession{
    private Player winner;
}
