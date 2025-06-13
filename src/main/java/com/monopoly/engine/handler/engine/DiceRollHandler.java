package com.monopoly.engine.handler.engine;

import com.monopoly.domain.engine.GameSession;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiceRollHandler implements EngineHandler{
    private Integer firstDice;
    private Integer secondDice;
    @Override
    public void handle(GameSession gameSession) {
        if(firstDice == secondDice) {
            gameSession.getCurrentPlayer().setComboTimes(
                    gameSession.getCurrentPlayer().getComboTimes() + 1
            );
        }
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return true;
    }
}
