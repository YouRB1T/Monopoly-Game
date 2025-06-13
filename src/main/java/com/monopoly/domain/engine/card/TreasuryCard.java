package com.monopoly.domain.engine.card;

import com.monopoly.domain.engine.enums.TreasuryCardType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TreasuryCard extends Card implements ITreasury{

    private TreasuryCardType type;
    private Integer value;

    public TreasuryCard(UUID id, String title, String description, TreasuryCardType type) {
        super(id, title, description);
        this.type = type;
    }

    @Override
    public TreasuryCardType getType() {
        return type;
    }
}
