package com.monopoly.engine.handler.engine;

import com.monopoly.domain.engine.dto.request.engine.DtoEndPlayerMoveRequest;
import com.monopoly.domain.engine.dto.response.engine.DtoEndPlayerMoveResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.service.GameSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class EndPlayerMoveHandler implements EngineHandler<DtoEndPlayerMoveResponse, DtoEndPlayerMoveRequest> {
    @Autowired
    private GameSessionService gameSessionService;

    @Override
    public DtoEndPlayerMoveResponse handle(DtoEndPlayerMoveRequest request) {
        GameSession gameSession = request.getGameSession();

        Player currentPlayer = gameSession.getCurrentPlayer();
        currentPlayer.setComboTimes(0);
        Player nextPlayer = gameSessionService.setNextPlayer(gameSession);
        log.info("Player " + nextPlayer.getName() + " is next player " + " session " + gameSession.getId());
        return new DtoEndPlayerMoveResponse(gameSession, nextPlayer);
    }

    @Override
    public boolean canHandle(DtoEndPlayerMoveRequest request) {
        return true;
    }
}
