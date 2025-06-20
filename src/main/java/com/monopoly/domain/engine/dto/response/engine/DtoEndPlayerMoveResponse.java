package com.monopoly.domain.engine.dto.response.engine;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DtoEndPlayerMoveResponse implements IDtoEngineHandlerResponse {
    private GameSession gameSession;
    private Player nextPlayer;

    @Override
    public List<String> getExecutedHandlers() {
        return List.of("END_PLR_MOVE");
    }
}
