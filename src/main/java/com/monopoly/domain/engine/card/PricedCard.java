package com.monopoly.domain.engine.card;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class PricedCard extends Card implements Price {

    @Override
    public Integer getPrice() {
        return 0;
    }
}
