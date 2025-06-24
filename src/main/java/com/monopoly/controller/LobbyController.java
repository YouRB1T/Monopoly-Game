package com.monopoly.controller;

import com.monopoly.domain.engine.Lobby;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.dto.request.lobby.CreateLobbyRequest;
import com.monopoly.domain.engine.dto.request.lobby.JoinLobbyRequest;
import com.monopoly.domain.engine.dto.response.lobby.CreateLobbyResponse;
import com.monopoly.domain.engine.dto.response.lobby.JoinLobbyResponse;
import com.monopoly.domain.entity.PlayerEntity;
import com.monopoly.service.LobbyHandlerService;
import com.monopoly.service.LobbyService;
import com.monopoly.service.PlayerEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private LobbyHandlerService lobbyHandlerService;
    
    @Autowired
    private PlayerEntityService playerEntityService;

    @PostMapping("/create")
    public ResponseEntity<CreateLobbyResponse> createLobby(@RequestBody CreateLobbyRequest request) {
        Lobby lobby = lobbyService.createLobby(request);

        String websocketUrl = "/lobby/" + lobby.getId();

        CreateLobbyResponse response = new CreateLobbyResponse(
                lobby.getId(),
                lobby,
                websocketUrl
        );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Lobby>> getAllLobbies() {
        List<Lobby> lobbies = lobbyService.getAllLobbies();
        return ResponseEntity.ok(lobbies);
    }

    @PostMapping("/{lobbyId}/join")
    public ResponseEntity<?> joinLobby(
            @PathVariable UUID lobbyId,
            @RequestBody JoinLobbyRequest request) {
        
        try {
            Lobby lobby = lobbyService.joinLobby(lobbyId, request.getPlayerName(), request.getPassword());

            Optional<PlayerEntity> playerEntityOpt = playerEntityService.getPlayerByNickname(request.getPlayerName());
            if (playerEntityOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Player not found"));
            }
            
            Player player = playerEntityService.convertToPlayer(playerEntityOpt.get(), 0);

            String websocketUrl = "/game/lobby/" + lobbyId;

            JoinLobbyResponse response = new JoinLobbyResponse(
                    lobbyId,
                    player,
                    lobby,
                    websocketUrl
            );

            lobbyHandlerService.broadcastPlayerJoined(lobby, player);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to join lobby", "message", e.getMessage()));
        }
    }
}
