package com.monopoly.domain.engine.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class Card implements Id {
    private final UUID id;
    private final String title;
    private final String description;

    @Override
    public UUID getId() {
        return id;
    }
}
