package com.monopoly.engine.handler.card;

import com.monopoly.domain.dto.request.card.DtoUseFreePrisonCardRequest;
import com.monopoly.domain.dto.response.card.DtoUseFreePrisonCardResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.Card;
import com.monopoly.domain.engine.card.special.PrisonCard;
import com.monopoly.service.PrisonService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Setter
public class UseFreePrisonCardHandler implements CardHandler<DtoUseFreePrisonCardResponse, DtoUseFreePrisonCardRequest>{

    @Autowired
    private PrisonService prisonService;

    @Override
    public DtoUseFreePrisonCardResponse handle(DtoUseFreePrisonCardRequest request) {
        Player player = request.getPlayer();
        PrisonCard card = request.getCard();
        GameSession gameSession = request.getGameSession();

        if (!prisonService.isInPrison(player, card)) {
            throw new IllegalStateException("Игрок не находится в тюрьме");
        }

        if (!prisonService.canUseFreePrisonCard(player)) {
            throw new IllegalStateException("У игрока нет карт освобождения из тюрьмы");
        }

        player.setFreePrisonCards(player.getFreePrisonCards() - 1);
        prisonService.releaseFromPrison(player, gameSession);

        return new DtoUseFreePrisonCardResponse(gameSession, player, card);

    }

    @Override
    public boolean canHandle(DtoUseFreePrisonCardRequest request) {
        return prisonService.isInPrison(request.getPlayer(), request.getCard())
                && prisonService.canUseFreePrisonCard(request.getPlayer());
    }
}
