package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.Card;
import com.monopoly.service.PlayerService;
import com.monopoly.service.PrisonService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoPrisonHandler implements CardHandler {
    private Player player;
    private Card prisonCard;
    @Override
    public void handle(GameSession gameSession) {
        PrisonService.sandToPrison(player, gameSession);
        System.out.println("Игрок " + player.getName() + " отправлен в тюрьму картой " + prisonCard.getTitle());
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return !player.isInPrison();
    }
}
