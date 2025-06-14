package com.monopoly.repository.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monopoly.domain.engine.Board;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.enums.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionTypeConverterTest {

    private GameSessionTypeConverter converter;
    private GameSession gameSession;

    @BeforeEach
    void setUp() {
        converter = new GameSessionTypeConverter();
        
        // Create a simple GameSession for testing
        UUID sessionId = UUID.randomUUID();
        UUID boardId = UUID.randomUUID();
        
        Player player1 = new Player("Player1", UUID.randomUUID(), 0, null);
        Player player2 = new Player("Player2", UUID.randomUUID(), 0, null);
        List<Player> players = Arrays.asList(player1, player2);
        
        Board board = new Board(boardId, new ArrayList<>(), new ArrayList<>(), 40, new ArrayList<>());
        List<Board> boards = Collections.singletonList(board);
        
        Map<Player, Integer> playerPositions = new HashMap<>();
        playerPositions.put(player1, 0);
        playerPositions.put(player2, 5);
        
        gameSession = new GameSession(
            sessionId, 
            boards, 
            players, 
            playerPositions, 
            new HashMap<>(), 
            new HashMap<>(), 
            player1,
            GameStatus.ACTIVE
        );
    }

    @Test
    void convertToDatabaseColumn_shouldSerializeGameSession() {
        // When
        String json = converter.convertToDatabaseColumn(gameSession);
        
        // Then
        assertNotNull(json);
        assertTrue(json.contains(gameSession.getId().toString()));
        assertTrue(json.contains("Player1"));
        assertTrue(json.contains("Player2"));
        assertTrue(json.contains("IN_PROGRESS"));
    }

    @Test
    void convertToDatabaseColumn_shouldReturnNullForNullInput() {
        // When
        String result = converter.convertToDatabaseColumn(null);
        
        // Then
        assertNull(result);
    }

    @Test
    void convertToEntityAttribute_shouldDeserializeToGameSession() {
        // Given
        String json = converter.convertToDatabaseColumn(gameSession);
        
        // When
        GameSession result = converter.convertToEntityAttribute(json);
        
        // Then
        assertNotNull(result);
        assertEquals(gameSession.getId(), result.getId());
        assertEquals(gameSession.getPlayers().size(), result.getPlayers().size());
        assertEquals(gameSession.getBoards().size(), result.getBoards().size());
        assertEquals(gameSession.getStatus(), result.getStatus());
    }

    @Test
    void convertToEntityAttribute_shouldReturnNullForNullOrEmptyInput() {
        // When
        GameSession result1 = converter.convertToEntityAttribute(null);
        GameSession result2 = converter.convertToEntityAttribute("");
        GameSession result3 = converter.convertToEntityAttribute("   ");
        
        // Then
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
    }

    @Test
    void convertToEntityAttribute_shouldThrowExceptionForInvalidJson() {
        // Given
        String invalidJson = "{invalid-json}";
        
        // When/Then
        assertThrows(RuntimeException.class, () -> 
            converter.convertToEntityAttribute(invalidJson)
        );
    }
}