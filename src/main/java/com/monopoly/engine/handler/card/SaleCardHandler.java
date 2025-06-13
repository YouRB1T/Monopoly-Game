package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.service.GameSessionService;
import com.monopoly.service.PropertyCardService;
import lombok.Setter;

@Setter
public class SaleCardHandler implements CardHandler{
    private Player oldOwner;
    private Player newOwner;
    private PropertyCard propertyCard;
    private Integer price;

    @Override
    public void handle(GameSession gameSession) {
        if (!PropertyCardService.getPropertyOwner(gameSession, propertyCard).equals(oldOwner)) {
            throw new IllegalStateException("Игрок не владеет данной картой");
        }

        if (newOwner.getMoneys() < price) {
            throw new IllegalStateException("У покупателя недостаточно средств");
        }

        GameSessionService.transferMoney(newOwner, oldOwner, price);

        PropertyCardService.transferProperty(gameSession, propertyCard, oldOwner, newOwner);

        System.out.println("Игрок " + newOwner.getName() + " купил карту " + propertyCard.getTitle() +
                " у игрока " + oldOwner.getName() + " за " + price);
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return PropertyCardService.getPropertyOwner(gameSession, propertyCard).equals(oldOwner)
                && newOwner.getMoneys() > price;
    }
}
