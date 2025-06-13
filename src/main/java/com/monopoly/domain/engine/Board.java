package com.monopoly.domain.engine;

import com.monopoly.domain.engine.card.*;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Board implements Id {
    private final UUID id;
    private final List<ChanceCard> chanceCards;
    private final List<TreasuryCard> treasuryCards;
    private final int size;
    private final List<BoardCard> cardsOnBoard;

    @Override
    public UUID getId() {
        return id;
    }
}
