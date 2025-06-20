package com.monopoly.domain.engine.dto.response.engine;

import com.monopoly.domain.engine.GameSession;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DtoStartPlayerMoveResponse implements IDtoEngineHandlerResponse{
    private GameSession gameSession;
    private Integer firstDice;
    private Integer secondDice;

    @Override
    public List<String> getExecutedHandlers() {
        return List.of("START_PLR_MOVE");
    }
}
