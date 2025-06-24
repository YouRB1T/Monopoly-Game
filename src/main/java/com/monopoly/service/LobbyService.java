package com.monopoly.service;

import com.monopoly.domain.engine.Lobby;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.dto.request.lobby.CreateLobbyRequest;
import com.monopoly.domain.entity.PlayerEntity;
import com.monopoly.repository.LobbyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class LobbyService {
    
    @Autowired
    private LobbyRepository lobbyRepository;
    
    @Autowired
    private PlayerEntityService playerEntityService;

    public Lobby createLobby(CreateLobbyRequest request) {
        UUID lobbyId = UUID.randomUUID();
        
        Lobby lobby = new Lobby(
                lobbyId,
                request.getLobbyName(),
                playerEntityService.convertToPlayer(request.getCreator(), 0),
                request.getGameRules(),
                request.getMaxPlayers(),
                request.getPassword()
        );
        log.info("Created lobby: {}", lobby);
        return lobbyRepository.create(lobby);
    }

    public List<Lobby> getAllLobbies() {
        log.info("Getting all lobbies");
        return lobbyRepository.findAll();
    }
    
    public Lobby getLobbyById(UUID lobbyId) {
        return lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new IllegalArgumentException("Лобби с ID " + lobbyId + " не найдено"));
    }
    
    public Lobby addPlayerToLobby(UUID lobbyId, Player player) {
        Lobby lobby = getLobbyById(lobbyId);
        
        if (lobby.getPlayers().size() >= lobby.getMaxPlayers()) {
            throw new IllegalStateException("Lobby is full");
        }
        
        lobby.getPlayers().add(player);
        log.info("Added player to lobby: {}", player);
        return lobbyRepository.update(lobby);
    }
    
    public Lobby removePlayerFromLobby(UUID lobbyId, UUID playerId) {
        Lobby lobby = getLobbyById(lobbyId);
        
        lobby.getPlayers().removeIf(player -> player.getId().equals(playerId));

        log.info("Removed player from lobby: {}", playerId);
        return lobbyRepository.update(lobby);
    }
    
    public void closeLobby(UUID lobbyId) {
        log.info("Closed lobby: {}", lobbyId);
        lobbyRepository.deleteById(lobbyId);
    }
    
    public Lobby updateLobbyPassword(UUID lobbyId, String newPassword) {
        Lobby lobby = getLobbyById(lobbyId);
        lobby.setPassword(newPassword);
        log.info("Updated lobby password: {}", lobbyId);
        return lobbyRepository.update(lobby);
    }
    
    public Lobby assignNewCreator(UUID lobbyId, UUID newCreatorId) {
        Lobby lobby = getLobbyById(lobbyId);
        
        Player newCreator = lobby.getPlayers().stream()
                .filter(player -> player.getId().equals(newCreatorId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Игрок с ID " + newCreatorId + " не найден в лобби"));
        
        lobby.setCreator(newCreator);
        log.info("Assigned new creator to lobby: {}", newCreatorId);
        return lobbyRepository.update(lobby);
    }

    public Lobby joinLobby(UUID lobbyId, String playerName, String password) {
        Lobby lobby = getLobbyById(lobbyId);
        
        if (lobby.getPlayers().size() >= lobby.getMaxPlayers()) {
            throw new IllegalStateException("Lobby is full");
        }

        if (lobby.getPassword() != null && !lobby.getPassword().isEmpty()) {
            if (!lobby.getPassword().equals(password)) {
                throw new IllegalArgumentException("Invalid password");
            }
        }

        Optional<PlayerEntity> playerEntityOpt = playerEntityService.getPlayerByNickname(playerName);
        if (playerEntityOpt.isEmpty()) {
            throw new IllegalArgumentException("Player not registered");
        }

        Player player = playerEntityService.convertToPlayer(playerEntityOpt.get(), 0);
        if (lobby.getPlayers().stream().anyMatch(p -> p.getId().equals(player.getId()))) {
            throw new IllegalStateException("Player already in lobby");
        }

        return addPlayerToLobby(lobbyId, player);
    }

}
