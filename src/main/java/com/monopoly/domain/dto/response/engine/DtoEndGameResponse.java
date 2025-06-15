package com.monopoly.domain.dto.response.engine;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DtoEndGameResponse implements IDtoEngineHandlerResponse{
    private GameSession gameSession;
    private Player winner;

    @Override
    public List<String> getExecutedHandlers() {
        return List.of("END_GAME");
    }

    @Override
    public GameSession getGameSession() {
        return null;
    }
}
