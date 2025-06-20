package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.dto.request.card.DtoChangeRentRequest;
import com.monopoly.domain.engine.dto.response.card.DtoChangeRentResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.service.PropertyCardService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Setter
public class ChangeRentHandler implements CardHandler<DtoChangeRentResponse, DtoChangeRentRequest>{
    @Autowired
    private PropertyCardService propertyCardService;

    @Override
    public DtoChangeRentResponse handle(DtoChangeRentRequest request) {
        GameSession gameSession = request.getGameSession();
        PropertyCard propertyCard = request.getPropertyCard();
        Integer newRentLevel = request.getNewRentLevel();

        if (!propertyCardService.isPropertyOwned(gameSession, propertyCard)) {
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
    public Class<? extends DtoChangeRentRequest> getSupportedRequestType() {
        return DtoChangeRentRequest.class;
    }

    @Override
    public boolean canHandle(DtoChangeRentRequest request) {
        return propertyCardService.isPropertyOwned(request.getGameSession(), request.getPropertyCard());
    }
}
