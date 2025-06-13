package com.monopoly.engine.handler.engine;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.service.GameSessionService;
import com.monopoly.service.PlayerService;

public class StartGameHandler implements EngineHandler{
    private Integer START_MONEYS = 2000;
    @Override
    public void handle(GameSession gameSession) {
        gameSession.getPlayers().stream()
                .forEach(player -> {
                    PlayerService.addMoneys(player, START_MONEYS);
                    GameSessionService.movePlayerToPosition(gameSession, player, 0);
                });
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return true;
    }
}
