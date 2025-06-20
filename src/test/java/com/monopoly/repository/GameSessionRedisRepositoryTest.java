package com.monopoly.repository;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameSessionRedisRepositoryTest {

    @Mock
    private RedisTemplate<String, GameSession> redisTemplate;
    
    @Mock
    private ValueOperations<String, GameSession> valueOperations;

    private GameSessionRedisRepository repository;
    
    private GameSession gameSession;
    private UUID sessionId;
    private String sessionKey;
    
    @BeforeEach
    void setUp() {
        // Инициализация игровой сессии
        sessionId = UUID.randomUUID();
        gameSession = new GameSession(new ArrayList<>(), sessionId, new ArrayList<>(), new TreeMap<>(), new HashMap<>());
        gameSession.setPropertyCardOwners(new TreeMap<>());
        
        sessionKey = "GameSession:" + sessionId;
        
        // Настройка моков
        repository = new GameSessionRedisRepository(redisTemplate);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }
    
    @Test
    @DisplayName("Должен создавать игровую сессию в Redis")
    void shouldCreateGameSession() {
        // Arrange
        doNothing().when(valueOperations).set(sessionKey, gameSession);
        
        // Act
        GameSession result = repository.create(gameSession);
        
        // Assert
        assertEquals(gameSession, result);
        verify(valueOperations).set(sessionKey, gameSession);
    }
    
    @Test
    @DisplayName("Должен находить игровую сессию по ID")
    void shouldFindGameSessionById() {
        // Arrange
        when(valueOperations.get(sessionKey)).thenReturn(gameSession);
        
        // Act
        Optional<GameSession> result = repository.findById(sessionId);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(gameSession, result.get());
        verify(valueOperations).get(sessionKey);
    }
    
    @Test
    @DisplayName("Должен возвращать пустой Optional, если сессия не найдена")
    void shouldReturnEmptyOptionalWhenSessionNotFound() {
        // Arrange
        when(valueOperations.get(sessionKey)).thenReturn(null);
        
        // Act
        Optional<GameSession> result = repository.findById(sessionId);
        
        // Assert
        assertFalse(result.isPresent());
        verify(valueOperations).get(sessionKey);
    }
    
    @Test
    @DisplayName("Должен удалять игровую сессию по ID")
    void shouldDeleteGameSessionById() {
        // Arrange
        when(valueOperations.get(sessionKey)).thenReturn(gameSession);
        when(redisTemplate.delete(sessionKey)).thenReturn(true);
        
        // Act
        GameSession result = repository.deleteById(sessionId);
        
        // Assert
        assertEquals(gameSession, result);
        verify(valueOperations).get(sessionKey);
        verify(redisTemplate).delete(sessionKey);
    }

    @Test
    @DisplayName("Должен проверять существование игровой сессии")
    void shouldCheckIfGameSessionExists() {
        // Arrange
        when(redisTemplate.hasKey(sessionKey)).thenReturn(true);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        repository.create(gameSession);
        // Act
        boolean result = repository.existsByID(sessionId);

        // Assert
        assertTrue(result);
        verify(redisTemplate).hasKey(sessionKey);
    }

}