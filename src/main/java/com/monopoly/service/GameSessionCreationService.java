package com.monopoly.service;

import com.monopoly.domain.engine.Board;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Lobby;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.enums.GameStatus;
import com.monopoly.domain.engine.enums.LobbyStatus;
import com.monopoly.repository.ActiveGameSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameSessionCreationService {
    
    private final ActiveGameSessionRepository gameSessionRepository;
    private final BoardService boardService;
    
    public String createGameSessionFromLobby(Lobby lobby, boolean isClassic) {
        UUID sessionId = UUID.randomUUID();

        List<Board> boards = boardService.createBoards(isClassic);

        Map<Player, Integer> playerPositions = new HashMap<>();

        GameSession gameSession = new GameSession(
                sessionId,
                boards,
                new ArrayList<>(lobby.getPlayers()),
                playerPositions,
                new HashMap<>(),
                new HashMap<>(),
                lobby.getGameRules(),
                null,
                GameStatus.ACTIVE
        );

        gameSessionRepository.create(gameSession);
        log.info("Created game session with id " + sessionId);
        lobby.setStatus(LobbyStatus.IN_GAME);
        return "/session/" + sessionId;
    }

}