package com.monopoly.service;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameSessionService {
    public static void transferMoney(Player from, Player to, Integer amount) {
        if (from.getMoneys() < amount) {
            throw new IllegalStateException("Недостаточно средств у игрока " + from.getName());
        }
        PlayerService.subMoneys(from, amount);
        PlayerService.addMoneys(to, amount);
    }

    public static void movePlayerToPosition(GameSession session, Player player, int newPosition) {
        session.getPlayerPosition().put(player, newPosition);
    }

    public int getCurrentPlayerPosition(GameSession session, Player player) {
        return session.getPlayerPosition().getOrDefault(player, 0);
    }

    public static Player setNextPlayer(GameSession session) {
        List<Player> players = session.getPlayers();
        int currentIndex = players.indexOf(session.getCurrentPlayer());
        int nextIndex = (currentIndex + 1) % players.size();
        session.setCurrentPlayer(players.get(nextIndex));
        return players.get(nextIndex);
    }
}
