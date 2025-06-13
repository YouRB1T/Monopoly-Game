package com.monopoly.domain.engine.card.special;

import com.monopoly.domain.engine.card.BoardCard;
import com.monopoly.engine.handler.card.CardHandler;

import java.util.List;
import java.util.UUID;

public class StartCard extends BoardCard {
    public StartCard(UUID id, String title, String description, List<CardHandler> cardHandlers, Integer cardPosition) {
        super(id, title, description, cardHandlers, cardPosition);
    }

}
