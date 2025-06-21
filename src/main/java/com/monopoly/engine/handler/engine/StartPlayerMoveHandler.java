package com.monopoly.engine.handler.engine;

import com.monopoly.domain.engine.dto.request.engine.DtoStartPlayerMoveRequest;
import com.monopoly.domain.engine.dto.response.engine.DtoStartPlayerMoveResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.service.GameSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartPlayerMoveHandler implements EngineHandler<DtoStartPlayerMoveResponse, DtoStartPlayerMoveRequest>{
    @Autowired
    private GameSessionService gameSessionService;
    private Player player;

    @Override
    public DtoStartPlayerMoveResponse handle(DtoStartPlayerMoveRequest request) {
        GameSession gameSession = request.getGameSession();
        Integer[] result = gameSessionService.rollDice();
        gameSessionService.movePlayerToPosition(gameSession, player,
                result[0] + result[1]);
        log.info("Player " + player.getName() + " rolled " + result[0] + " and " + result[1] + " session " + gameSession.getId());
        return new DtoStartPlayerMoveResponse(gameSession, result[0], result[1]);
    }

    @Override
    public boolean canHandle(DtoStartPlayerMoveRequest request) {
        return true;
    }
}
