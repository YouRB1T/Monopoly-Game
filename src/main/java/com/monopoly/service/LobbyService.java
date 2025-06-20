package com.monopoly.service;

import com.monopoly.domain.engine.Lobby;
import com.monopoly.domain.engine.Player;
import com.monopoly.dto.request.CreateLobbyRequest;
import com.monopoly.repository.LobbyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LobbyService {
    
    private final LobbyRepository lobbyRepository;
    
    public Lobby createLobby(CreateLobbyRequest request) {
        UUID lobbyId = UUID.randomUUID();
        
        Lobby lobby = new Lobby(
                lobbyId,
                request.getLobbyName(),
                request.getCreator(),
                request.getGameRules(),
                request.getMaxPlayers(),
                request.getPassword()
        );
        
        return lobbyRepository.create(lobby);
    }
    
    public Lobby getLobbyById(UUID lobbyId) {
        return lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new IllegalArgumentException("Лобби с ID " + lobbyId + " не найдено"));
    }
    
    public Lobby addPlayerToLobby(UUID lobbyId, Player player) {
        Lobby lobby = getLobbyById(lobbyId);
        
        if (lobby.getPlayers().size() >= lobby.getMaxPlayers()) {
            throw new IllegalStateException("Лобби заполнено");
        }
        
        lobby.getPlayers().add(player);
        return lobbyRepository.update(lobby);
    }
    
    public Lobby removePlayerFromLobby(UUID lobbyId, UUID playerId) {
        Lobby lobby = getLobbyById(lobbyId);
        
        lobby.getPlayers().removeIf(player -> player.getId().equals(playerId));
        
        return lobbyRepository.update(lobby);
    }
    
    public void closeLobby(UUID lobbyId) {
        lobbyRepository.deleteById(lobbyId);
    }
    
    public Lobby updateLobbyPassword(UUID lobbyId, String newPassword) {
        Lobby lobby = getLobbyById(lobbyId);
        lobby.setPassword(newPassword);
        return lobbyRepository.update(lobby);
    }
    
    public Lobby assignNewCreator(UUID lobbyId, UUID newCreatorId) {
        Lobby lobby = getLobbyById(lobbyId);
        
        Player newCreator = lobby.getPlayers().stream()
                .filter(player -> player.getId().equals(newCreatorId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Игрок с ID " + newCreatorId + " не найден в лобби"));
        
        lobby.setCreator(newCreator);
        return lobbyRepository.update(lobby);
    }
}
