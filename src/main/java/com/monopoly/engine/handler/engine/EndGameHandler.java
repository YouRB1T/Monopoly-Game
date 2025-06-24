package com.monopoly.engine.handler.engine;

import com.monopoly.domain.engine.dto.request.engine.DtoEndGameRequest;
import com.monopoly.domain.engine.dto.response.engine.DtoEndGameResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;

@Getter
@Setter
@Slf4j
public class EndGameHandler implements EngineHandler<DtoEndGameResponse, DtoEndGameRequest> {

    @Override
    public DtoEndGameResponse handle(DtoEndGameRequest request) {
        GameSession gameSession = request.getGameSession();
        Player winner = gameSession.getPlayers().stream()
               .max(Comparator.comparingInt(Player::getTotalMoneys))
               .orElse(null);
        log.info("Game ended. Winner: " + winner.getName() + " session " + gameSession.getId());
        return new DtoEndGameResponse(gameSession, winner, new ArrayList<>());
    }

    @Override
    public boolean canHandle(DtoEndGameRequest request) {
        return request.getGameSession().getPlayers().stream()
                .anyMatch(player -> player.getTotalMoneys() <= 0);
    }
}
