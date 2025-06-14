package com.monopoly.engine.handler.engine;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.service.GameSessionService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class StartPlayerMoveHandler implements EngineHandler{
    @Autowired
    private GameSessionService gameSessionService;
    private DiceRollHandler diceRollHandler;
    private Player player;

    @Override
    public void handle(GameSession gameSession) {
        diceRollHandler.handle(gameSession);
        gameSessionService.movePlayerToPosition(gameSession, player,
                diceRollHandler.getFirstDice() + diceRollHandler.getSecondDice());
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return false;
    }
}
