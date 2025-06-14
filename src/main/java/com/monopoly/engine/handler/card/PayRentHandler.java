package com.monopoly.engine.handler.card;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.request.card.DtoPayRentRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;
import com.monopoly.domain.dto.response.card.DtoPayRentResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.service.GameSessionService;
import com.monopoly.service.PropertyCardService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static java.rmi.server.LogStream.log;

@Slf4j
@RequiredArgsConstructor
@Setter
public class PayRentHandler implements CardHandler<DtoPayRentResponse, DtoPayRentRequest> {
    @Autowired
    private GameSessionService gameSessionService;

    @Override
    public DtoPayRentResponse handle(DtoPayRentRequest request) {
        Player player = request.getPlayer();
        GameSession gameSession = request.getGameSession();
        PropertyCard propertyCard = request.getPropertyCard();

        Player owner = PropertyCardService.getPropertyOwner(gameSession, propertyCard);
        if (owner == null || owner.equals(player)) {
            return new DtoPayRentResponse(gameSession, player, propertyCard, owner, 0);
        }

        Integer rent = PropertyCardService.calculateRent(propertyCard);
        gameSessionService.transferMoney(player, owner, rent);

        log("Игрок " + player.getName() + " заплатил аренду " + rent +
                " игроку " + owner.getName() + " за карту " + propertyCard.getTitle());
        return new DtoPayRentResponse(gameSession, player, propertyCard, owner, rent);
    }

    @Override
    public boolean canHandle(DtoPayRentRequest request) {
        Player owner = PropertyCardService.getPropertyOwner(request.getGameSession(), request.getPropertyCard());
        return owner != null || !owner.equals(request.getPlayer());
    }
}
