package com.monopoly.domain.entity;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.repository.converter.GameSessionTypeConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_session")
public class GameSessionEntity  {

    @Id
    private UUID id;

    @Column(name = "session_data")
    @Convert(converter = GameSessionTypeConverter.class)
    private GameSession sessionData;

    @Column(name = "archived_at", updatable = false)
    private LocalDateTime archivedAt;

    @PrePersist
    protected void onCreate() {
        archivedAt = LocalDateTime.now();
    }

    public GameSessionEntity() {}

    public GameSessionEntity(GameSession sessionData) {
        this.id = sessionData.getId();
        this.sessionData = sessionData;
    }

    public UUID getId() { return id; }
    public GameSession getSessionData() { return sessionData; }
    public LocalDateTime getArchivedAt() { return archivedAt; }

    protected void setId(UUID id) { this.id = id; }
    protected void setSessionData(GameSession sessionData) { this.sessionData = sessionData; }
    protected void setArchivedAt(LocalDateTime archivedAt) { this.archivedAt = archivedAt; }

}
