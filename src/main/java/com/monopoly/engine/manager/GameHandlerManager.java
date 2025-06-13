package com.monopoly.engine.manager;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.engine.handler.GameHandler;
import com.monopoly.engine.handler.card.BuyPropertyCardHandler;
import com.monopoly.engine.handler.card.ChanceHandler;
import com.monopoly.engine.handler.engine.*;
import com.monopoly.websocet.massage.session.GameHandlerMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class GameHandlerManager implements HandlerManager{
    private final List<GameHandler> handlers;
    @Override
    public List<GameHandler> getAllHandles() {
        return handlers;
    }

    @Override
    public void handleEvent(GameHandlerMessage massage, GameSession gameSession) {
        GameHandler gameHandler = null;

        switch (massage.getHandleType()){
            // Engine
            case STRT_GAME ->
                    gameHandler = new StartGameHandler();
            case END_GAME ->
                    gameHandler = new EndGameHandler();
            case STRT_PLR_MOVE ->
                    gameHandler = new StartPlayerMoveHandler();
            case END_PLR_MOVE ->
                    gameHandler = new EndPlayerMoveHandler();
            case ROLL_DICE ->
                    gameHandler = new DiceRollHandler();
            // Card
            case BUY_PRPRTY ->
                    gameHandler = new BuyPropertyCardHandler();
            case CHANCE ->
                    gameHandler = new ChanceHandler();
            case CHNG_RENT ->
                gameHandler = new ChanceHandler();
            case GO_PRSN ->
                gameHandler = new ChanceHandler();

        }
    }
}
