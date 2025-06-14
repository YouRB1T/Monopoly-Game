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
    public static boolean isInPrison(Player player, PrisonCard prisonCard) {
        return player.isInPrison() && prisonCard.getInPrisonPLayers().contains(player);
    }

    public static void giveFreePrisonCard(Player player) {
        player.setFreePrisonCards(player.getFreePrisonCards() + 1);
    }

    public static Integer findPositionPrisonCard(Board board) {
        Integer position = 0;
        for (BoardCard card : board.getCardsOnBoard()) {
            if (card instanceof Prisoned) {
                return position;
            }
            position++;
        }
        return null;
    }

    public static PrisonCard getPrisonCard(Board board) {
        return (PrisonCard) board.getCardsOnBoard().get(findPositionPrisonCard(board));
    }

    public static void sandToPrison(Player player, GameSession gameSession) {
        Integer pos = findPositionPrisonCard(gameSession.getBoards().get(0));
        PrisonCard prisonCard = getPrisonCard(gameSession.getBoards().get(0));

        prisonCard.addPlayerToPrison(player);
        gameSession.getPlayerPosition().put(player, pos);
    }

    public static void releaseFromPrison(Player player, GameSession gameSession) {
        player.setInPrison(false);
        getPrisonCard(gameSession.getBoards().get(0)).freePLayer(player);
    }

    public static boolean canUseFreePrisonCard(Player player) {
        return player.getFreePrisonCards() != 0;
    }

}
