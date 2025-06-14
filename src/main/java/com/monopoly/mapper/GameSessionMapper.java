package com.monopoly.mapper;

import com.monopoly.domain.engine.Board;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.Card;

import java.util.UUID;

public class GameSessionMapper {
    private final GameSession gameSession;
    private GameSessionRepository repository;

    public GameSessionMapper(UUID gameSession) {
        this.gameSession = ;
    }

    public Player getPLayerById(UUID playerId) {
        return gameSession.getPlayers().stream()
                .filter(player -> player.getId().equals(playerId))
                .findFirst().orElseThrow(
                        () -> new IllegalArgumentException("ERROR: PLayer not found: " + playerId)
                );
    }

    public Card getCardById(UUID cardId) {
        return gameSession.getBoards().get(0).getCardsOnBoard()
                .stream().filter(card -> card.getId().equals(cardId))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("ERROR: Card not found: " + cardId)
                );
    }

    public Board getBoardById(UUID boardId) {
        return gameSession.getBoards().stream()
                .filter(board -> board.getId().equals(boardId))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("ERROR: Board not find " + boardId)
                );
    }

}
