package com.monopoly.service;

import com.monopoly.domain.engine.Board;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.Prisoned;
import com.monopoly.domain.engine.card.BoardCard;
import com.monopoly.domain.engine.card.special.PrisonCard;
import org.springframework.stereotype.Service;

@Service
public class PrisonService {
    public boolean isInPrison(Player player, PrisonCard prisonCard) {
        return player.isInPrison() && prisonCard.getInPrisonPLayers().contains(player);
    }

    public void giveFreePrisonCard(Player player) {
        player.setFreePrisonCards(player.getFreePrisonCards() + 1);
    }

    public Integer findPositionPrisonCard(Board board) {
        Integer position = 0;
        for (BoardCard card : board.getCardsOnBoard()) {
            if (card instanceof Prisoned) {
                return position;
            }
            position++;
        }
        return null;
    }

    public PrisonCard getPrisonCard(Board board) {
        return (PrisonCard) board.getCardsOnBoard().get(findPositionPrisonCard(board));
    }

    public void sandToPrison(Player player, GameSession gameSession) {
        Integer pos = findPositionPrisonCard(gameSession.getBoards().get(0));
        PrisonCard prisonCard = getPrisonCard(gameSession.getBoards().get(0));

        prisonCard.addPlayerToPrison(player);
        gameSession.getPlayerPosition().put(player, pos);
    }

    public void releaseFromPrison(Player player, GameSession gameSession) {
        player.setInPrison(false);
        getPrisonCard(gameSession.getBoards().get(0)).freePLayer(player);
    }

    public boolean canUseFreePrisonCard(Player player) {
        return player.getFreePrisonCards() != 0;
    }

}
