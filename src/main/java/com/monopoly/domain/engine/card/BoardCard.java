package com.monopoly.domain.engine.card;

import com.monopoly.engine.handler.card.CardHandler;

import java.util.List;
import java.util.UUID;

public class BoardCard extends Card implements IPosition, IHandlers{
    private final List<CardHandler> cardHandlers;
    private final Integer cardPosition;

    public BoardCard(UUID id, String title, String description,
                     List<CardHandler> cardHandlers, Integer cardPosition) {
        super(id, title, description);
        this.cardHandlers = cardHandlers;
        this.cardPosition = cardPosition;
    }


    @Override
    public Integer getPosition() {
        return cardPosition;
    }

    @Override
    public List<CardHandler> getHandlers() {
        return cardHandlers;
    }
}
