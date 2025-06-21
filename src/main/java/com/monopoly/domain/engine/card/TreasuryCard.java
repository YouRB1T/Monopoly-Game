package com.monopoly.domain.engine.card;

import com.monopoly.domain.engine.enums.TreasuryCardType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TreasuryCard extends Card implements ITreasury{

    private TreasuryCardType type;
    private Integer value;

    public TreasuryCard(UUID id, String title, String description, TreasuryCardType type, Integer value) {
        super(id, title, description);
        this.type = type;
        this.value = value;
    }

    @Override
    public TreasuryCardType getType() {
        return type;
    }
}
