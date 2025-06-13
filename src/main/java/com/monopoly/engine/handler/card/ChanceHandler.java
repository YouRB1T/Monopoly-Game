package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.ChanceCard;
import com.monopoly.domain.engine.card.special.PrisonCard;
import com.monopoly.service.GameSessionService;
import com.monopoly.service.PlayerService;
import com.monopoly.service.PrisonService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Random;

@Setter
@RequiredArgsConstructor
public class ChanceHandler implements CardHandler {
    private final Random random = new Random();

    private PrisonCard prisonCard;
    private Player player;

    @Override
    public void handle(GameSession gameSession) {
        List<ChanceCard> chanceCards = gameSession.getBoards().get(0).getChanceCards();
        if (chanceCards.isEmpty()) {
            return;
        }

        ChanceCard chanceCard = chanceCards.get(random.nextInt(chanceCards.size()));

        switch (chanceCard.getType()) {
            case GO_TO_PRISON:
                PrisonService.sandToPrison(player, gameSession);
                System.out.println("Игрок " + player.getName() + " отправлен в тюрьму");
                break;
            case FREE_PRISON_CARD:
                PrisonService.giveFreePrisonCard(player);
                System.out.println("Игрок " + player.getName() + " получил карту освобождения из тюрьмы");
                break;
        }
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return gameSession.getBoards().get(0).getChanceCards().isEmpty();
    }
}
