package com.monopoly.domain.engine.dto.response.engine;

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
    private List<String> executedHandlers;

    @Override
    public List<String> getExecutedHandlers() {
        executedHandlers.add("END_GAME");
        return executedHandlers;
    }
}
