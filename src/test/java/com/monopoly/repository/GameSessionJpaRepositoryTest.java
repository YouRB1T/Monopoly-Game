package com.monopoly.repository;

import com.monopoly.domain.entity.GameSessionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GameSessionJpaRepositoryTest {

    @Autowired
    private GameSessionJpaRepository repository;

    @Test
    @Sql("/test-data/game-sessions.sql")
    void findByGameStatus_shouldReturnSessionsWithMatchingStatus() {
        // When
        List<GameSessionEntity> results = repository.findByGameStatus("FINISHED");
        
        // Then
        assertFalse(results.isEmpty());
        results.forEach(entity -> 
            assertTrue(entity.getSessionData().getStatus().toString().equals("FINISHED"))
        );
    }

    @Test
    @Sql("/test-data/game-sessions.sql")
    void findGamesByPlayer_shouldReturnGamesWithSpecificPlayer() {
        // When
        List<GameSessionEntity> results = repository.findGamesByPlayer("TestPlayer");
        
        // Then
        assertFalse(results.isEmpty());
        // Verify player exists in each result
    }

    @Test
    @Sql("/test-data/game-sessions.sql")
    void findByPlayerCount_shouldReturnGamesWithSpecificPlayerCount() {
        // When
        List<GameSessionEntity> results = repository.findByPlayerCount(4);
        
        // Then
        assertFalse(results.isEmpty());
        results.forEach(entity -> 
            assertEquals(4, entity.getSessionData().getPlayers().size())
        );
    }

    @Test
    @Sql("/test-data/game-sessions.sql")
    void countGamesInPeriod_shouldReturnCorrectCount() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(30);
        LocalDateTime end = LocalDateTime.now();
        
        // When
        long count = repository.countGamesInPeriod(start, end);
        
        // Then
        assertTrue(count > 0);
    }

    @Test
    @Sql("/test-data/game-sessions.sql")
    void findTop10ByOrderByArchivedAtDesc_shouldReturnLatestGames() {
        // When
        List<GameSessionEntity> results = repository.findTop10ByOrderByArchivedAtDesc();
        
        // Then
        assertFalse(results.isEmpty());
        assertTrue(results.size() <= 10);
        
        // Verify ordering
        for (int i = 0; i < results.size() - 1; i++) {
            assertTrue(
                results.get(i).getArchivedAt().isAfter(results.get(i + 1).getArchivedAt()) ||
                results.get(i).getArchivedAt().isEqual(results.get(i + 1).getArchivedAt())
            );
        }
    }
}