package com.monopoly.service;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.enums.GameStatus;
import com.monopoly.domain.entity.GameSessionEntity;
import com.monopoly.repository.GameSessionJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class GameSessionEntityServiceIntegrationTest {


    private GameSessionJpaRepository repository;
    
    @Autowired
    private GameSessionEntityService service;
    
    @Test
    @DisplayName("Должен архивировать завершенную игровую сессию")
    void shouldArchiveFinishedGameSession() {
        // Arrange
        Player player = new Player(UUID.randomUUID(), 1500, new HashSet<>());

        GameSession gameSession = new GameSession(List.of(player), UUID.randomUUID(),
                new ArrayList<>(), new TreeMap<>(), new HashMap<>());
        
        GameSessionEntity savedEntity = new GameSessionEntity(gameSession);
        when(repository.save(any(GameSessionEntity.class))).thenReturn(savedEntity);
        
        // Act
        service.archiveGameSession(gameSession);
        
        // Assert
        verify(repository).save(any(GameSessionEntity.class));
    }
    
    @Test
    @DisplayName("Должен выбрасывать исключение при архивировании null сессии")
    void shouldThrowExceptionWhenArchivingNullSession() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> service.archiveGameSession(null)
        );
        
        assertTrue(exception.getMessage().contains("Нельзя заархивировать null сессию"));
    }
    
    @Test
    @DisplayName("Должен выбрасывать исключение при архивировании незавершенной сессии")
    void shouldThrowExceptionWhenArchivingUnfinishedSession() {
        // Arrange
        Player player = new Player(UUID.randomUUID(), 1500, new HashSet<>());

        GameSession gameSession = new GameSession(List.of(player), UUID.randomUUID(),
                new ArrayList<>(), new TreeMap<>(), new HashMap<>());
        
        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class, 
            () -> service.archiveGameSession(gameSession)
        );
        
        assertTrue(exception.getMessage().contains("Пытаешься заархивировать незавершенную игру"));
    }
}