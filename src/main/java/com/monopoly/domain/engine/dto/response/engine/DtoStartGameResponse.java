package com.monopoly.domain.engine.dto.response.engine;

import com.monopoly.domain.engine.GameSession;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DtoStartGameResponse implements IDtoEngineHandlerResponse{
    private GameSession gameSession;
    private List<String> executedHandlers;

    @Override
    public GameSession getGameSession() {
        return gameSession;
    }
}
