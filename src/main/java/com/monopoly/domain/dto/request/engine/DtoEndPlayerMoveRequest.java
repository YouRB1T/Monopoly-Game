package com.monopoly.domain.dto.request.engine;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoEndPlayerMoveRequest implements IDtoEngineHandlerRequest{
    private GameSession gameSession;
    private Player currentPlayer;

    @Override
    public GameSession getGameSession() {
        return gameSession;
    }
}
