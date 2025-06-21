package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.dto.request.card.DtoUseFreePrisonCardRequest;
import com.monopoly.domain.engine.dto.response.card.DtoUseFreePrisonCardResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.special.PrisonCard;
import com.monopoly.service.PrisonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UseFreePrisonCardHandler implements CardHandler<DtoUseFreePrisonCardResponse, DtoUseFreePrisonCardRequest>{

    @Autowired
    private PrisonService prisonService;

    @Override
    public DtoUseFreePrisonCardResponse handle(DtoUseFreePrisonCardRequest request) {
        Player player = request.getPlayer();
        PrisonCard card = request.getCard();
        GameSession gameSession = request.getGameSession();

        if (!prisonService.isInPrison(player, card)) {
            log.info("Player is not in prison" + " session " + gameSession.getId());
            throw new IllegalStateException("Player is not in prison");
        }

        if (!prisonService.canUseFreePrisonCard(player)) {
            log.info("Player has no free prison cards" + " session " + gameSession.getId());
            throw new IllegalStateException("Player has no free prison cards");
        }

        player.setFreePrisonCards(player.getFreePrisonCards() - 1);
        prisonService.releaseFromPrison(player, gameSession);

        log.info("Player " + player.getName() + " used free prison card" + " session " + gameSession.getId());

        return new DtoUseFreePrisonCardResponse(gameSession, player, card);

    }

    @Override
    public Class<? extends DtoUseFreePrisonCardRequest> getSupportedRequestType() {
        return DtoUseFreePrisonCardRequest.class;
    }

    @Override
    public boolean canHandle(DtoUseFreePrisonCardRequest request) {
        return prisonService.isInPrison(request.getPlayer(), request.getCard())
                && prisonService.canUseFreePrisonCard(request.getPlayer());
    }
}
