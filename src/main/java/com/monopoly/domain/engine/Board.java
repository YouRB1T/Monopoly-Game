package com.monopoly.domain.engine;

import com.monopoly.domain.engine.card.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Board implements Id {
    private final UUID id;
    private final List<ChanceCard> chanceCards;
    private final List<TreasuryCard> treasuryCards;
    private final int size;
    private final List<BoardCard> cardsOnBoard;

    public Board(UUID id, List<ChanceCard> chanceCards, List<TreasuryCard> treasuryCards,
                 int size, List<BoardCard> cardsOnBoard) {
        this.id = id;
        this.chanceCards = chanceCards;
        this.treasuryCards = treasuryCards;
        this.size = size;
        this.cardsOnBoard = cardsOnBoard;
    }

    @Override
    public UUID getId() {
        return id;
    }
}
