package com.monopoly.repository;

import com.monopoly.domain.engine.Lobby;

import java.util.Optional;
import java.util.UUID;

public interface LobbyRepository {
    Lobby create(Lobby lobby);
    Optional<Lobby> findById(UUID id);
    Lobby update(Lobby lobby);
    Lobby deleteById(UUID id);
    boolean existsById(UUID id);
}