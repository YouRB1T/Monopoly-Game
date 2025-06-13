package com.monopoly.domain.engine.card;

import com.monopoly.domain.engine.enums.ChanceCardType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ChanceCard extends Card implements IChance{
    private final ChanceCardType type;
    @Setter
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
