package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.ChanceCard;
import com.monopoly.domain.engine.card.TreasuryCard;
import com.monopoly.service.PlayerService;
import lombok.Setter;

import java.util.List;
import java.util.Random;

@Setter
public class TreasuryHandler implements CardHandler{
    private Player player;
    private Random random;

    @Override
    public void handle(GameSession gameSession) {
        List<TreasuryCard> treasuryCards = gameSession.getBoards().get(0).getTreasuryCards();
        if (treasuryCards.isEmpty()) {
            return;
        }

        TreasuryCard treasuryCard = treasuryCards.get(random.nextInt(treasuryCards.size()));

        switch (treasuryCard.getType()) {
            case MONEY_REWARD:
                PlayerService.addMoneys(player, treasuryCard.getValue());
                System.out.println("Игрок " + player.getName() + " получил " + treasuryCard.getValue() + " денег");
                break;
            case MONEY_PENALTY:
                PlayerService.subMoneys(player, treasuryCard.getValue());
                System.out.println("Игрок " + player.getName() + " заплатил штраф " + treasuryCard.getValue());
                break;
        }
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return gameSession.getBoards().get(0).getTreasuryCards().isEmpty();
    }
}
