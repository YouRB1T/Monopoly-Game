package com.monopoly.service;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.enums.GameStatus;
import com.monopoly.domain.entity.GameSessionEntity;
import com.monopoly.repository.GameSessionJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameSessionEntityServiceTest {

    @Mock
    private GameSessionJpaRepository repository;

    @InjectMocks
    private GameSessionEntityService service;

    private GameSession gameSession;
    private UUID sessionId;
    private GameSessionEntity entity;

    @BeforeEach
    void setUp() {
        sessionId = UUID.randomUUID();
        gameSession = mock(GameSession.class);
        when(gameSession.getId()).thenReturn(sessionId);
        when(gameSession.getStatus()).thenReturn(GameStatus.FINISHED);
        
        entity = new GameSessionEntity(gameSession);
    }

    @Test
    void archiveGameSession_shouldSaveFinishedGame() {
        // When
        service.archiveGameSession(gameSession);
        
        // Then
        verify(repository).save(any(GameSessionEntity.class));
    }

    @Test
    void archiveGameSession_shouldThrowExceptionForNullSession() {
        // When/Then
        assertThrows(IllegalArgumentException.class, () -> 
            service.archiveGameSession(null)
        );
    }

    @Test
    void archiveGameSession_shouldThrowExceptionForUnfinishedGame() {
        // Given
        when(gameSession.getStatus()).thenReturn(GameStatus.ACTIVE);
        
        // When/Then
        assertThrows(IllegalStateException.class, () -> 
            service.archiveGameSession(gameSession)
        );
    }

    @Test
    void findArchivedSession_shouldReturnGameSessionWhenExists() {
        // Given
        when(repository.findById(sessionId)).thenReturn(Optional.of(entity));
        
        // When
        Optional<GameSession> result = service.findArchivedSession(sessionId);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(gameSession, result.get());
    }

    @Test
    void findArchivedSession_shouldReturnEmptyOptionalWhenNotExists() {
        // Given
        when(repository.findById(sessionId)).thenReturn(Optional.empty());
        
        // When
        Optional<GameSession> result = service.findArchivedSession(sessionId);
        
        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void findPlayerGameHistory_shouldReturnPlayerGames() {
        // Given
        String playerName = "TestPlayer";
        List<GameSessionEntity> entities = Arrays.asList(entity);
        when(repository.findGamesByPlayer(playerName)).thenReturn(entities);
        
        // When
        List<GameSession> results = service.findPlayerGameHistory(playerName);
        
        // Then
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(gameSession, results.get(0));
    }

    @Test
    void getRecentGames_shouldReturnLatestGames() {
        // Given
        List<GameSessionEntity> entities = Arrays.asList(entity);
        when(repository.findTop10ByOrderByArchivedAtDesc()).thenReturn(entities);
        
        // When
        List<GameSession> results = service.getRecentGames();
        
        // Then
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(gameSession, results.get(0));
    }

    @Test
    void isGameArchived_shouldReturnTrueWhenExists() {
        // Given
        when(repository.existsById(sessionId)).thenReturn(true);
        
        // When
        boolean result = service.isGameArchived(sessionId);
        
        // Then
        assertTrue(result);
    }

    @Test
    void isGameArchived_shouldReturnFalseWhenNotExists() {
        // Given
        when(repository.existsById(sessionId)).thenReturn(false);
        
        // When
        boolean result = service.isGameArchived(sessionId);
        
        // Then
        assertFalse(result);
    }
}