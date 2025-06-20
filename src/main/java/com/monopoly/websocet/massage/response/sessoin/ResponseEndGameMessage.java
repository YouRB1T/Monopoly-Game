package com.monopoly.websocet.massage.response.sessoin;

import com.monopoly.domain.engine.Player;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class ResponseEndGameMessage extends ResponseWebSocketMessageSession {
    private Player winner;

    public ResponseEndGameMessage(UUID sessionId, Player winner) {
        super(sessionId);
        this.winner = winner;
    }
}
