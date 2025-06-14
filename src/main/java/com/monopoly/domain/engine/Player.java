package com.monopoly.domain.engine;

import com.monopoly.domain.engine.card.Card;
import com.monopoly.domain.engine.card.Prisoned;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Player implements Id, Prisoned {
    private final UUID id;
    private String name;
    private Integer moneys;
    private Integer totalMoneys;
    private final Set<Card> playerCards;
    private boolean inPrison;
    private Integer comboTimes;
    private Integer prisonTurns;
    private Integer freePrisonCards;

    public Player(String name, UUID id, Integer moneys, Set<Card> playerCards) {
        this.name = name;
        this.id = id;
        this.moneys = moneys;
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
