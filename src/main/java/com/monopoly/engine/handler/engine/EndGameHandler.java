package com.monopoly.engine.handler.engine;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.request.engine.DtoEndGameRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;
import com.monopoly.domain.dto.response.engine.DtoEndGameResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
public class EndGameHandler implements EngineHandler<DtoEndGameResponse, DtoEndGameRequest> {

    @Override
    public DtoEndGameResponse handle(DtoEndGameRequest request) {
        GameSession gameSession = request.getGameSession();
        Player winner = gameSession.getPlayers().stream()
               .max(Comparator.comparingInt(Player::getTotalMoneys))
               .orElse(null);
        return new DtoEndGameResponse(gameSession, winner);
    }

    @Override
    public boolean canHandle(DtoEndGameRequest request) {
        return request.getGameSession().getPlayers().stream()
                .anyMatch(player -> player.getTotalMoneys() <= 0);
    }
}
