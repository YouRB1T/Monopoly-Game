package com.monopoly.domain.engine.card;

import com.monopoly.domain.engine.enums.ChanceCardType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ChanceCard extends Card implements IChance{
    private final ChanceCardType type;
    private Integer value;

    public ChanceCard(UUID id, String title, String description, ChanceCardType type) {
        super(id, title, description);
        this.type = type;
    }

    @Override
    public ChanceCardType getType() {
        return type;
    }
}
