package com.monopoly.engine.handler;

import com.monopoly.domain.engine.GameSession;

public interface GameHandler {
    boolean canHandle(GameSession gameSession);
}
