package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.Card;
import com.monopoly.domain.engine.card.special.PrisonCard;
import com.monopoly.service.PrisonService;
import lombok.Setter;

@Setter
public class UseFreePrisonCardHandler implements CardHandler{
    private Player player;
    private PrisonCard card;
    @Override
    public void handle(GameSession gameSession) {
        if (!PrisonService.isInPrison(player, card)) {
            throw new IllegalStateException("Игрок не находится в тюрьме");
        }

        if (!PrisonService.canUseFreePrisonCard(player)) {
            throw new IllegalStateException("У игрока нет карт освобождения из тюрьмы");
        }

        player.setFreePrisonCards(player.getFreePrisonCards() - 1);
        PrisonService.releaseFromPrison(player, gameSession);

    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return PrisonService.isInPrison(player, card) && PrisonService.canUseFreePrisonCard(player);
    }
}
