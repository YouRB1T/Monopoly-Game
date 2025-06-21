package com.monopoly.engine.handler.engine;

import com.monopoly.domain.engine.dto.request.engine.DtoStartGameRequest;
import com.monopoly.domain.engine.dto.response.engine.DtoStartGameResponse;
import com.monopoly.service.GameSessionService;
import com.monopoly.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class StartGameHandler implements EngineHandler<DtoStartGameResponse, DtoStartGameRequest>{
    @Autowired
    private PlayerService playerService;
    @Autowired
    private GameSessionService gameSessionService;

    @Override
    public DtoStartGameResponse handle(DtoStartGameRequest request) {
        request.getGameSession().getPlayers().stream()
                .forEach(player -> {
                    playerService.addMoneys(player,
                            (Integer) request.getGameSession().getGameRules().get("START_MONEYS"));
                    gameSessionService.movePlayerToPosition(request.getGameSession(), player, 0);
                });
        log.info("Game started " + " session " + request.getGameSession().getId());
        return new DtoStartGameResponse(request.getGameSession(), List.of("START_GAME"));
    }

    @Override
    public boolean canHandle(DtoStartGameRequest request) {
        return true;
    }
}
