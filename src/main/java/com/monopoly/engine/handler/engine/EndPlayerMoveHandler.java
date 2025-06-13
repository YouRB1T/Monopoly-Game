package com.monopoly.engine.handler.engine;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.service.GameSessionService;
import com.monopoly.service.PlayerService;

public class EndPlayerMoveHandler implements EngineHandler {
    private Player currentPlayer;
    private Player nextPlayer;
    @Override
    public void handle(GameSession gameSession) {
        currentPlayer = gameSession.getCurrentPlayer();
        currentPlayer.setComboTimes(0);
        nextPlayer = GameSessionService.setNextPlayer(gameSession);
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return true;
    }
}
