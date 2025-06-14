package com.monopoly.engine.handler.card;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.request.card.DtoChangeRentRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;
import com.monopoly.domain.dto.response.card.DtoChangeRentResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.service.PropertyCardService;
import lombok.Setter;

@Setter
public class ChangeRentHandler implements CardHandler<DtoChangeRentResponse, DtoChangeRentRequest>{

    @Override
    public DtoChangeRentResponse handle(DtoChangeRentRequest request) {
        GameSession gameSession = request.getGameSession();
        PropertyCard propertyCard = request.getPropertyCard();
        Integer newRentLevel = request.getNewRentLevel();

        if (!PropertyCardService.isPropertyOwned(gameSession, propertyCard)) {
            throw new IllegalStateException("Собственность никому не принадлежит");
        }

        propertyCard.getRentOfCard().setCurrentRentLevel(newRentLevel);
        propertyCard.getRentOfCard().setCurrentRent(
                propertyCard.getRentOfCard().getRentLevels().get(newRentLevel));

        System.out.println("Уровень аренды для карты " + propertyCard.getTitle() +
                " изменен на " + newRentLevel);

        return new DtoChangeRentResponse(gameSession, propertyCard, newRentLevel);
    }

    @Override
    public boolean canHandle(DtoChangeRentRequest request) {
        return PropertyCardService.isPropertyOwned(request.getGameSession(), request.getPropertyCard());
    }
}
