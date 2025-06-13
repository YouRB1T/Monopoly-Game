package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.service.PropertyCardService;
import lombok.Setter;

@Setter
public class ChangeRentHandler implements CardHandler{
    private PropertyCard propertyCard;
    private Integer newRentLevel;

    @Override
    public void handle(GameSession gameSession) {

        if (!PropertyCardService.isPropertyOwned(gameSession, propertyCard)) {
            throw new IllegalStateException("Собственность никому не принадлежит");
        }

        propertyCard.getRentOfCard().setCurrentRentLevel(newRentLevel);
        propertyCard.getRentOfCard().setCurrentRent(
                propertyCard.getRentOfCard().getRentLevels().get(newRentLevel));

        System.out.println("Уровень аренды для карты " + propertyCard.getTitle() +
                " изменен на " + newRentLevel);
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return PropertyCardService.isPropertyOwned(gameSession, propertyCard);
    }
}
