package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.dto.request.card.DtoSaleCardRequest;
import com.monopoly.domain.engine.dto.response.card.DtoSaleCardResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.service.GameSessionService;
import com.monopoly.service.PropertyCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SaleCardHandler implements CardHandler<DtoSaleCardResponse, DtoSaleCardRequest>{

    @Autowired
    private GameSessionService gameSessionService;
    @Autowired
    private PropertyCardService propertyCardService;

    @Override
    public DtoSaleCardResponse handle(DtoSaleCardRequest request) {
        GameSession gameSession = request.getGameSession();
        PropertyCard propertyCard = request.getPropertyCard();
        Player oldOwner = request.getOldOwner();
        Player newOwner = request.getNewOwner();
        Integer price = request.getPrice();

        if (!propertyCardService.getPropertyOwner(gameSession, propertyCard).equals(oldOwner)) {
            log.info("Player is not owner of property" + " session " + gameSession.getId());
            throw new IllegalStateException("Player is not owner of property");
        }

        if (newOwner.getMoneys() < price) {
            log.info("Buyer has not enough money" + " session " + gameSession.getId());
            throw new IllegalStateException("Buyer has not enough money");
        }

        gameSessionService.transferMoney(newOwner, oldOwner, price);

        propertyCardService.transferProperty(gameSession, propertyCard, oldOwner, newOwner);

        log.info("Player " + newOwner.getName() + " bought property " + propertyCard.getTitle() +
                " from " + oldOwner.getName() + " for " + price + " session " + gameSession.getId());

        return new DtoSaleCardResponse(gameSession, oldOwner, newOwner, propertyCard, price);
    }

    @Override
    public Class<? extends DtoSaleCardRequest> getSupportedRequestType() {
        return DtoSaleCardRequest.class;
    }

    @Override
    public boolean canHandle(DtoSaleCardRequest request) {
        return propertyCardService.getPropertyOwner(
                request.getGameSession(), request.getPropertyCard()).equals(request.getOldOwner())
                && request.getNewOwner().getMoneys() > request.getPrice();
    }
}
