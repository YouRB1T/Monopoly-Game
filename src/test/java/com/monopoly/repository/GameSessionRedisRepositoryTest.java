package com.monopoly.repository;

import com.monopoly.domain.engine.GameSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameSessionRedisRepositoryTest {

    @Mock
    private RedisTemplate<UUID, GameSession> redisTemplate;
    
    @Mock
    private ValueOperations<UUID, GameSession> valueOperations;
    
    private GameSessionRedisRepository repository;
    private GameSession gameSession;
    private UUID sessionId;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        repository = new GameSessionRedisRepository(redisTemplate);
        sessionId = UUID.randomUUID();
        gameSession = mock(GameSession.class);
        when(gameSession.getId()).thenReturn(sessionId);
    }

    @Test
    void create_shouldSaveGameSessionToRedis() {
        // When
        GameSession result = repository.create(gameSession);
        
        // Then
        verify(valueOperations).set(any(UUID.class), eq(gameSession));
        assertEquals(gameSession, result);
    }

    @Test
    void findById_shouldReturnGameSessionWhenExists() {
        // Given
        when(valueOperations.get(any(UUID.class))).thenReturn(gameSession);
        
        // When
        Optional<GameSession> result = repository.findById(sessionId);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(gameSession, result.get());
    }

    @Test
    void findById_shouldReturnEmptyOptionalWhenNotExists() {
        // Given
        when(valueOperations.get(any(UUID.class))).thenReturn(null);
        
        // When
        Optional<GameSession> result = repository.findById(sessionId);
        
        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void deleteById_shouldRemoveGameSessionAndReturnIt() {
        // Given
        when(valueOperations.get(any(UUID.class))).thenReturn(gameSession);
        
        // When
        GameSession result = repository.deleteById(sessionId);
        
        // Then
        verify(redisTemplate).delete(any(UUID.class));
        assertEquals(gameSession, result);
    }

    @Test
    void existsById_shouldReturnTrueWhenExists() {
        // Given
        when(redisTemplate.hasKey(any(UUID.class))).thenReturn(true);
        
        // When
        boolean result = repository.existsByID(sessionId);
        
        // Then
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalseWhenNotExists() {
        // Given
        when(redisTemplate.hasKey(any(UUID.class))).thenReturn(false);
        
        // When
        boolean result = repository.existsByID(sessionId);
        
        // Then
        assertFalse(result);
    }
}