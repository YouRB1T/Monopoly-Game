package com.monopoly.repository;

import com.monopoly.domain.engine.GameSession;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

public interface ActiveGameSessionRepository {
    GameSession create(GameSession gameSession);

    Optional<GameSession> findById(UUID id);

    GameSession deleteById(UUID id);

    boolean existsByID(UUID id);
}