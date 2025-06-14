package com.monopoly.repository;

import com.monopoly.domain.entity.GameSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface GameSessionJpaRepository extends JpaRepository<GameSessionEntity, UUID> {

    @Query(value = "SELECT * FROM game_session_archive WHERE session_data->>'status' = :status", nativeQuery = true)
    List<GameSessionEntity> findByGameStatus(@Param("status") String status);

    @Query(value = """
        SELECT * FROM game_session_archive 
        WHERE session_data->'players' @> '[{"name": :playerName}]'::jsonb
        ORDER BY archived_at DESC
        """, nativeQuery = true)
    List<GameSessionEntity> findGamesByPlayer(@Param("playerName") String playerName);

    @Query(value = "SELECT * FROM game_session_archive WHERE jsonb_array_length(session_data->'players') = :playerCount", nativeQuery = true)
    List<GameSessionEntity> findByPlayerCount(@Param("playerCount") int playerCount);

    // Статистика по играм за период
    @Query(value = """
        SELECT COUNT(*) FROM game_session_archive 
        WHERE archived_at BETWEEN :startDate AND :endDate
        """, nativeQuery = true)
    long countGamesInPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<GameSessionEntity> findTop10ByOrderByArchivedAtDesc();
}
