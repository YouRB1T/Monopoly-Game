package com.monopoly.engine;

import com.monopoly.domain.dto.request.DtoHandlerRquest;
import com.monopoly.domain.dto.request.engine.DtoStartGameRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;
import com.monopoly.domain.dto.response.engine.DtoStartGameResponse;
import org.springframework.stereotype.Service;

@Service
public class GameSessionEngineService implements GameSessionEngine{

    public DtoStartGameResponse startGame(DtoStartGameRequest request) {

    }

    @Override
    public DtoHandlerResponse handleEvent(DtoHandlerRquest request) {

        return null;
    }
}
