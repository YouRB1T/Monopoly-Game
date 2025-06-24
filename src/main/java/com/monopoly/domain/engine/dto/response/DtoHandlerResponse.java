package com.monopoly.domain.engine.dto.response;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.dto.response.card.IDtoCardHandlerResponse;

public interface DtoHandlerResponse {
    GameSession getGameSession();
}
