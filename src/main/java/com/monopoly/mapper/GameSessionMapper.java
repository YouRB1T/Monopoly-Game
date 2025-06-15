package com.monopoly.mapper;

import com.monopoly.domain.engine.Board;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.Card;
import com.monopoly.repository.GameSessionRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class GameSessionMapper {
    private GameSession gameSession;
    @Autowired
    private GameSessionRedisRepository repository;

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

    public GameSession getGameSessionById(UUID gameSessionId) {
        Optional<GameSession> gameSession = repository.findById(gameSessionId);
        gameSession.ifPresent(session -> this.gameSession = session);
        return this.gameSession;
    }

}
