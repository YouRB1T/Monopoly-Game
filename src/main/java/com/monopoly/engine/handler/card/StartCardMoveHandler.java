package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.service.PlayerService;

public class StartCardMoveHandler implements CardHandler{
    private Player player;
    private final Integer REWORD_FOR_CIRCLE;

    public StartCardMoveHandler(Integer rewordForCircle) {
        REWORD_FOR_CIRCLE = rewordForCircle;
    }

    @Override
    public void handle(GameSession gameSession) {
        PlayerService.addMoneys(player, REWORD_FOR_CIRCLE);
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return true;
    }
}
