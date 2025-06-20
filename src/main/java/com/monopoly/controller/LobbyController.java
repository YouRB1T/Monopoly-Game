package com.monopoly.controller;

import com.monopoly.domain.engine.Lobby;
import com.monopoly.dto.request.CreateLobbyRequest;
import com.monopoly.dto.response.CreateLobbyResponse;
import com.monopoly.service.LobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lobby")
@RequiredArgsConstructor
public class LobbyController {
    
    private final LobbyService lobbyService;
    
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
}