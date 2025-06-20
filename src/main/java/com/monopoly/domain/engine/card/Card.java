package com.monopoly.domain.engine.card;

import com.monopoly.domain.engine.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
public class Card implements Id {
    private final UUID id;
    private final String title;
    private final String description;

    public Card(UUID id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Override
    public UUID getId() {
        return id;
    }
}
