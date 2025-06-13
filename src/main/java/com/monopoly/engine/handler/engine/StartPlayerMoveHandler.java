package com.monopoly.engine.handler.engine;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.service.GameSessionService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartPlayerMoveHandler implements EngineHandler{
    private DiceRollHandler diceRollHandler;
    private Player player;

    @Override
    public void handle(GameSession gameSession) {
        diceRollHandler.handle(gameSession);
        GameSessionService.movePlayerToPosition(gameSession, player,
                diceRollHandler.getFirstDice() + diceRollHandler.getSecondDice());
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return false;
    }
}
