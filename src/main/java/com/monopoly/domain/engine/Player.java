package com.monopoly.domain.engine;

import com.monopoly.domain.engine.card.Card;
import com.monopoly.domain.engine.card.Prisoned;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Id, Prisoned {
    private UUID id;
    private String name;
    private Integer moneys;
    private Integer totalMoneys;
    private Set<Card> playerCards;
    private boolean inPrison;
    private Integer comboTimes = 0;
    private Integer prisonTurns = 0;
    private Integer freePrisonCards = 0;

    public Player(UUID id, Integer moneys, Set<Card> playerCards) {
        this.id = id;
        this.moneys = moneys;
        this.totalMoneys = moneys;
        this.playerCards = playerCards;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isInPrison() {
        return inPrison;
    }
}
