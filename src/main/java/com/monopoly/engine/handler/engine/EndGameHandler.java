package com.monopoly.engine.handler.engine;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
public class EndGameHandler implements EngineHandler {
    private Player winner;
    @Override
    public void handle(GameSession gameSession) {
       winner = gameSession.getPlayers().stream()
               .max(Comparator.comparingInt(Player::getTotalMoneys))
               .orElse(null);
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return gameSession.getPlayers().stream()
                .anyMatch(player -> player.getTotalMoneys() <= 0);
    }
}
