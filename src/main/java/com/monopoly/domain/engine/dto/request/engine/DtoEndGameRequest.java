package com.monopoly.domain.engine.dto.request.engine;

import com.monopoly.domain.engine.GameSession;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoEndGameRequest implements IDtoEngineHandlerRequest{
    private GameSession gameSession;

    @Override
    public GameSession getGameSession() {
        return null;
    }
}
