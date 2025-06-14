package com.monopoly.engine.handler.engine;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.service.GameSessionService;
import com.monopoly.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;

public class StartGameHandler implements EngineHandler{
    @Autowired
    private PlayerService playerService;
    @Autowired
    private GameSessionService gameSessionService;
    private Integer START_MONEYS = 2000;
    @Override
    public void handle(GameSession gameSession) {
        gameSession.getPlayers().stream()
                .forEach(player -> {
                    playerService.addMoneys(player, START_MONEYS);
                    gameSessionService.movePlayerToPosition(gameSession, player, 0);
                });
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return true;
    }
}
