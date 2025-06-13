package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.service.GameSessionService;
import com.monopoly.service.PropertyCardService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
public class PayRentHandler implements CardHandler {

    private Player player;
    private PropertyCard propertyCard;

    @Override
    public void handle(GameSession gameSession) {
        Player owner = PropertyCardService.getPropertyOwner(gameSession, propertyCard);
        if (owner == null || owner.equals(player)) {
            return;
        }

        Integer rent = PropertyCardService.calculateRent(propertyCard);
        GameSessionService.transferMoney(player, owner, rent);

        System.out.println("Игрок " + player.getName() + " заплатил аренду " + rent +
                " игроку " + owner.getName() + " за карту " + propertyCard.getTitle());
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        Player owner = PropertyCardService.getPropertyOwner(gameSession, propertyCard);
        return owner != null || !owner.equals(player);
    }
}
