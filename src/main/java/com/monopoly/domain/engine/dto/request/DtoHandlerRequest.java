package com.monopoly.domain.engine.dto.request;

import com.monopoly.domain.engine.GameSession;

import java.util.UUID;

public interface DtoHandlerRequest {
    GameSession getGameSession();
}
