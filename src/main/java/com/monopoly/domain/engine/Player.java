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

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isInPrison() {
        return inPrison;
    }
}
