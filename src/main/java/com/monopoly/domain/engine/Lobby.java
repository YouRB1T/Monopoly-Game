package com.monopoly.domain.engine;

import com.monopoly.domain.engine.enums.LobbyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Lobby implements Serializable {
    private UUID id;  // убираем final
    private String name;
    private Player creator;
    private List<Player> players;
    private Map<String, Object> gameRules;
    private Integer maxPlayers;
    private String password;
    private LobbyStatus status;

    public Lobby(UUID id, String name, Player creator, Map<String, Object> gameRules, Integer maxPlayers, String password) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.players = new ArrayList<>();
        this.players.add(creator);
        this.gameRules = gameRules;
        this.maxPlayers = maxPlayers;
        this.password = password;
        this.status = LobbyStatus.WAITING;
    }
}
