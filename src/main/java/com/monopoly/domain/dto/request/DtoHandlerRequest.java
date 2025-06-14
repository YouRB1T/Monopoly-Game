package com.monopoly.domain.dto.request;

import com.monopoly.domain.engine.GameSession;

import java.util.UUID;

public interface DtoHandlerRequest {
    GameSession getGameSession();
}
