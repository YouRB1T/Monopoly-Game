package com.monopoly.domain.engine;

import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.domain.engine.card.PropertyGroup;
import com.monopoly.domain.engine.enums.GameStatus;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
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
    private GameStatus status;
}

