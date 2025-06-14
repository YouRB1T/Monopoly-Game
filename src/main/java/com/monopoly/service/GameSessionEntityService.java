package com.monopoly.service;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.enums.GameStatus;
import com.monopoly.domain.entity.GameSessionEntity;
import com.monopoly.repository.GameSessionJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GameSessionEntityService {
    private final GameSessionJpaRepository repository;

    public GameSessionEntityService(GameSessionJpaRepository repository) {
        this.repository = repository;
    }

    public void archiveGameSession(GameSession gameSession) {
        if (gameSession == null) {
            throw new IllegalArgumentException("Нельзя заархивировать null сессию, гений!");
        }

        if (gameSession.getStatus() != GameStatus.FINISHED) {
            throw new IllegalStateException("Пытаешься заархивировать незавершенную игру? Статус: " + gameSession.getStatus());
        }

        GameSessionEntity entity = new GameSessionEntity(gameSession);
        repository.save(entity);
    }

    public Optional<GameSession> findArchivedSession(UUID sessionId) {
        return repository.findById(sessionId)
                .map(GameSessionEntity::getSessionData);
    }

    public List<GameSession> findPlayerGameHistory(String playerName) {
        return repository.findGamesByPlayer(playerName)
                .stream()
                .map(GameSessionEntity::getSessionData)
                .collect(Collectors.toList());
    }

    public List<GameSession> getRecentGames() {
        return repository.findTop10ByOrderByArchivedAtDesc()
                .stream()
                .map(GameSessionEntity::getSessionData)
                .collect(Collectors.toList());
    }

    public boolean isGameArchived(UUID sessionId) {
        return repository.existsById(sessionId);
    }
}
