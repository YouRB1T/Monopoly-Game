package com.monopoly.service;

import com.monopoly.domain.engine.Board;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Lobby;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.enums.GameStatus;
import com.monopoly.repository.ActiveGameSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GameSessionCreationService {
    
    private final ActiveGameSessionRepository gameSessionRepository;
    private final BoardService boardService;
    
    public String createGameSessionFromLobby(Lobby lobby, boolean isClassic) {
        UUID sessionId = UUID.randomUUID();
        
        // Создаем игровые доски
        List<Board> boards = boardService.createBoards(isClassic);
        
        // Инициализируем карты позиций игроков
        Map<Player, Integer> playerPositions = new HashMap<>();
        lobby.getPlayers().forEach(player -> playerPositions.put(player, 0));
        
        // Создаем игровую сессию
        GameSession gameSession = new GameSession(
                sessionId,
                boards,
                new ArrayList<>(lobby.getPlayers()),
                playerPositions,
                new HashMap<>(), // propertyCardOwners
                new HashMap<>(), // propertyGroups
                lobby.getGameRules(),
                lobby.getPlayers().get(0), // Первый игрок начинает
                GameStatus.ACTIVE
        );
        
        // Сохраняем сессию
        gameSessionRepository.create(gameSession);
        
        // Возвращаем URL для WebSocket
        return "/session/" + sessionId;
    }

}