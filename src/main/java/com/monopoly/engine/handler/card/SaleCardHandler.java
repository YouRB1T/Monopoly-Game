package com.monopoly.engine.handler.card;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.request.card.DtoSaleCardRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;
import com.monopoly.domain.dto.response.card.DtoSaleCardResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.service.GameSessionService;
import com.monopoly.service.PropertyCardService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Setter
public class SaleCardHandler implements CardHandler<DtoSaleCardResponse, DtoSaleCardRequest>{

    @Autowired
    private GameSessionService gameSessionService;

    @Override
    public DtoSaleCardResponse handle(DtoSaleCardRequest request) {
        GameSession gameSession = request.getGameSession();
        PropertyCard propertyCard = request.getPropertyCard();
        Player oldOwner = request.getOldOwner();
        Player newOwner = request.getNewOwner();
        Integer price = request.getPrice();

        if (!PropertyCardService.getPropertyOwner(gameSession, propertyCard).equals(oldOwner)) {
            throw new IllegalStateException("Игрок не владеет данной картой");
        }

        if (newOwner.getMoneys() < price) {
            throw new IllegalStateException("У покупателя недостаточно средств");
        }

        gameSessionService.transferMoney(newOwner, oldOwner, price);

        PropertyCardService.transferProperty(gameSession, propertyCard, oldOwner, newOwner);

        System.out.println("Игрок " + newOwner.getName() + " купил карту " + propertyCard.getTitle() +
                " у игрока " + oldOwner.getName() + " за " + price);

        return new DtoSaleCardResponse(gameSession, oldOwner, newOwner, propertyCard, price);
    }

    @Override
    public boolean canHandle(DtoSaleCardRequest request) {
        return PropertyCardService.getPropertyOwner(
                request.getGameSession(), request.getPropertyCard()).equals(request.getOldOwner())
                && request.getNewOwner().getMoneys() > request.getPrice();
    }
}
