package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.dto.request.card.DtoPayRentRequest;
import com.monopoly.domain.engine.dto.response.card.DtoPayRentResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.service.GameSessionService;
import com.monopoly.service.PropertyCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.rmi.server.LogStream.log;

@Component
@Slf4j
public class PayRentHandler implements CardHandler<DtoPayRentResponse, DtoPayRentRequest> {
    @Autowired
    private GameSessionService gameSessionService;
    @Autowired
    private PropertyCardService propertyCardService;

    @Override
    public DtoPayRentResponse handle(DtoPayRentRequest request) {
        Player player = request.getPlayer();
        GameSession gameSession = request.getGameSession();
        PropertyCard propertyCard = request.getPropertyCard();

        Player owner = propertyCardService.getPropertyOwner(gameSession, propertyCard);
        if (owner == null || owner.equals(player)) {
            return new DtoPayRentResponse(gameSession, player, propertyCard, owner, 0);
        }

        Integer rent = propertyCardService.calculateRent(propertyCard);
        gameSessionService.transferMoney(player, owner, rent);

        log.info("Player " + player.getName() + " paid rent " + rent +
                " to player " + owner.getName() + " for property " + propertyCard.getTitle() + " session " + gameSession.getId());
        return new DtoPayRentResponse(gameSession, player, propertyCard, owner, rent);
    }

    @Override
    public Class<? extends DtoPayRentRequest> getSupportedRequestType() {
        return DtoPayRentRequest.class;
    }

    @Override
    public boolean canHandle(DtoPayRentRequest request) {
        Player owner = propertyCardService.getPropertyOwner(request.getGameSession(), request.getPropertyCard());
        return owner != null || !owner.equals(request.getPlayer());
    }
}
