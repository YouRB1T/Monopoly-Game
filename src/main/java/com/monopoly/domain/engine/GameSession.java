package com.monopoly.domain.engine;

import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.domain.engine.card.PropertyGroup;
import com.monopoly.domain.engine.enums.GameStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class GameSession implements Serializable {
    private final UUID id;
    private final List<Board> boards;
    private final List<Player> players;
    private Map<Player, Integer> playerPosition;
    private Map<PropertyCard, Player> propertyCardOwners;
    private final Map<String, PropertyGroup> propertyGroups;
    private final Map<String, Object> gameRules;
    private Player currentPlayer;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GameStatus status;

    public GameSession(List<Player> players, UUID id, List<Board> boards, Map<String, Object> gameRules,
                       Map<String, PropertyGroup> propertyGroups) {
        this.players = players;
        this.id = id;
        this.boards = boards;
        this.gameRules = gameRules;
        this.propertyGroups = propertyGroups;
    }

    public GameSession(UUID id, List<Board> boards, List<Player> players,
                       Map<Player, Integer> playerPosition,
                       Map<PropertyCard, Player> propertyCardOwners,
                       Map<String, PropertyGroup> propertyGroups,
                       Map<String, Object> gameRules, Player currentPlayer,
                       GameStatus status) {
        this.id = id;
        this.boards = boards;
        this.players = players;
        this.playerPosition = playerPosition;
        this.propertyCardOwners = propertyCardOwners;
        this.propertyGroups = propertyGroups;
        this.gameRules = gameRules;
        this.currentPlayer = currentPlayer;
        this.status = status;
    }
}

